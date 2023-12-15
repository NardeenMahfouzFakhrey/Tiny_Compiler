package com.example.tiny_compiler;

import java.util.ArrayList;


public class Parser {

    ArrayList<Token> tokens = new ArrayList<>();
    int count = 0;
    int size = 0;
    TreeNode root;

    public Parser(ArrayList<Token> tokens) {
        size = tokens.size();
        this.tokens = tokens;
    }

    public boolean match(Token.TokenType currentToken, Token.TokenType expectedToken) {
        if (currentToken == expectedToken) {
            return true;
        }
        return false;
    }

    public Token getNextToken() {
        count++;
        return tokens.get(count);
    }

    //program→ stmt-sequence
    public void program() {
        root = new TreeNode("start", "START");
        root.setChild(stmtSequence(tokens.get(0)));
    }

    //statement→if- stmt,repeat-stmt,assign-stmt,read-stmt,write-stmt
    public TreeNode statement(Token token) {
        TreeNode nodeTemp = null;
        switch (token.getType()) {
            case IF: {
                nodeTemp = iFStmt(token);
                break;
            }
            case REPEAT: {
                nodeTemp = repeatStmt(token);
                break;
            }
            case READ: {
                nodeTemp = readStmt(token);
                break;
            }
            case WRITE: {
                nodeTemp = writeStmt(token);
                break;
            }
            case IDENTIFIER: {
                nodeTemp = assignStmt(token);
                break;
            }
            default: //throw error or what to do ??
        }
        return nodeTemp;
    }

    //stmt-sequence → stmt-sequence ; statement  statement
    public TreeNode stmtSequence(Token token) {
        TreeNode nodeTemp;
        Token nextToken = null;

        nodeTemp = statement(token);

        if (count < size) {
            nextToken = getNextToken();
        } else {
            //throw error missing semicolumn
        }

        while (nextToken.getType() == Token.TokenType.SEMI_COLON && count < size) {
            nextToken = getNextToken();
            nodeTemp.setChild(statement(nextToken));
            //return from statmenet with what token to handle these??
        }

        //what about handling error here
        return nodeTemp;
    }


    //if -stmt → if exp then stmt-sequence {else stmt-sequence} end
    public TreeNode iFStmt(Token token) {
        TreeNode nodeTemp = new TreeNode(token.getValue(), String.valueOf(token.getType()));
        Token nextToken = null;

        //value of current token advance what you need
        nodeTemp.setChild(exp(token));

        if (count < size) {
            nextToken = getNextToken();
        } else {
            //error missing then statement
        }

        if (!match(nextToken.getType(), Token.TokenType.THEN)) {
            //throwerror
        }

        nodeTemp.setChild(stmtSequence(nextToken));


        //advance input or already advanced
        if (count < size) {
            nextToken = getNextToken();
        } else {
            //missing end of if
        }

        if (nextToken.getType() == Token.TokenType.ELSE) {
            nodeTemp.setChild(stmtSequence(nextToken));
        } else if (nextToken.getType() == Token.TokenType.END) {
            return nodeTemp;
        } else {
            //error missing end
        }

        nextToken = getNextToken();
        if (!match(nextToken.getType(), Token.TokenType.END)) {
            //throwerror missing end
        }

        return nodeTemp;
    }



    /** errors to handle
     * 1- if count < size
     * 2- if any non-terminal function returns error
     * 3- if two tokens does not match
     * **/

    public TreeNode repeatStmt(Token token) {
        Token temp = null;

        TreeNode repeatRoot = new TreeNode(token.getValue(), String.valueOf(token.getType()));

        TreeNode bodyNode = null, testNode = null;

        /* check if next token exists */
        if (count < size) {
            temp = getNextToken();
        } else {
            //throw error missing stmt-seq
        }

        bodyNode = stmtSequence(temp);

        if (bodyNode != null) {
            repeatRoot.setChild(bodyNode);

            if (count < size) {
                temp = getNextToken();
            } else {
                //throw error missing until
            }

            if (match(temp.getType(), Token.TokenType.UNTIL)) {

                if (count < size) {
                    temp = getNextToken();
                } else {
                    //throw error missing exp
                }

                testNode = exp(temp);

                if (testNode != null) {
                    repeatRoot.setChild(testNode);
                } else {

                    // handle error if testNode = null or error
                }
            }
            else{
                // handle if next token is not until
            }
        }
        else {
            //handle error if bodyNode = null or error
        }
        return repeatRoot;
    }


    public TreeNode assignStmt(Token token) {
        Token temp = null;
        TreeNode assignRoot = new TreeNode(token.getValue(), String.valueOf(token.getType()));
        TreeNode expNode = null;

        if (count < size) {
            temp = getNextToken();
        } else {
            //throw error missing identifier
        }

        if(match(temp.getType(),Token.TokenType.IDENTIFIER)){
            if (count < size) {
                temp = getNextToken();
            } else {
                //throw error missing :=
            }

            if(match(temp.getType(),Token.TokenType.ASSIGN)){
                if (count < size) {
                    temp = getNextToken();
                } else {
                    //throw error missing exp
                }
                expNode=exp(temp);

                if(expNode != null){
                    assignRoot.setChild(expNode);
                }
                else{
                    //handle if exp returns error
                }

            }
            else{
                // next token of identifier is not as assign
            }

        }
        else{
            //handle if next token is not an identifier
        }

            return assignRoot;
    }


    public TreeNode readStmt(Token token) {
        Token temp = null;
        if (count < size) {
            temp = getNextToken();
        } else {
            //throw error missing identifier
        }
        //fixme : we passed here name of identifier as value to be prnited in tree
        TreeNode readRoot = new TreeNode(temp.getValue(), String.valueOf(token.getType()));

        if(!match(temp.getType(), Token.TokenType.IDENTIFIER)){
            //handle error next token is not an identifier
        }
        return readRoot;
    }

    public TreeNode writeStmt(Token token) {
        Token temp = null;
        TreeNode writeRoot =  new TreeNode(token.getValue(), String.valueOf(token.getType()));
        TreeNode expNode = null;

        if (count < size) {
            temp = getNextToken();
        } else {
            //throw error missing exp
        }
        expNode = exp(temp);

        if(expNode != null){
            writeRoot.setChild(expNode);
        }
        else{
            //handle return error from exp
        }
        return writeRoot;
    }


    // exp -> simple-exp comparison-op simple-exp | simple-exp
    public TreeNode exp(Token token) {
        TreeNode nodeTemp = simpleexp(token);

        // Check if there is a comparison operator
        Token nextToken = null;
        if (count < size) {
            nextToken = getNextToken();
        } else {
            // error: missing comparison operator
        }

        // comparison operator???
        if (nextToken.getType() == Token.TokenType.EQUAL) {

            TreeNode comparisonNode = new TreeNode(nextToken.getValue(), String.valueOf(nextToken.getType()));
            comparisonNode.setChild(nodeTemp);

            if (count < size) {
                nextToken = getNextToken();
            } else {
                // error: missing simple-exp after comparison operator
            }

            comparisonNode.setChild(simpleexp(nextToken));

            return comparisonNode;
        }

        return nodeTemp;
    }


    //simple-exp -> simple-exp addop term | term
    public TreeNode simpleexp(Token token) {
        TreeNode nodeTemp;
        Token nextToken = null;

        nodeTemp = term(token);

        if (count < size) {
            nextToken = getNextToken();
        } else {
            //error missing addop
        }

        while ( (nextToken.getType() == Token.TokenType.PLUS || nextToken.getType() == Token.TokenType.MINUS) && count < size) {
            nextToken = getNextToken();
            nodeTemp.setChild(term(nextToken));
        }
        return nodeTemp;
    }


    //term -> term mulop factor | factor
    public TreeNode term(Token token) {
        TreeNode nodeTemp;
        Token nextToken = null;

        nodeTemp = factor(token);

        if (count < size) {
            nextToken = getNextToken();
        } else {
            //error missing addop
        }

        while ( (nextToken.getType() == Token.TokenType.MULT || nextToken.getType() == Token.TokenType.DIV) && count < size) {
            nextToken = getNextToken();
            nodeTemp.setChild(factor(nextToken));
        }
        return nodeTemp;
    }

    // factor -> (exp) | number | identifier
    public TreeNode factor(Token token) {
        TreeNode nodeTemp = null;

        switch (token.getType()) {
            case OPENBRACKET: {

                if (count < size) {
                    Token nextToken = getNextToken();
                    nodeTemp = exp(nextToken);

                    // Check for the closing parenthesis
                    if (count < size) {
                        Token closingbracket = getNextToken();
                        if (match(closingbracket.getType(), Token.TokenType.CLOSEDBRACKET)) {
                        } else {
                            // Error: Missing closing parenthesis
                        }
                    } else {
                        // Error: Missing closing parenthesis
                    }
                } else {
                    // Error: Missing expression after '('
                }
                break;
            }
            case NUMBER:
            case IDENTIFIER: {
                nodeTemp = new TreeNode(token.getValue(), String.valueOf(token.getType()));
                break;
            }
            default: {
                // Error: Invalid factor
                break;
            }
        }
        return nodeTemp;
    }


}
