package com.example.tiny_compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Parser {

    ArrayList<Token> tokens = new ArrayList<>();
    int count = 0;
    int size = 0;
    TreeNode root;

    Queue<Token> queue;


    /**
     * create queue to hold tokens
     *
     * @param tokens
     */
    public Parser(ArrayList<Token> tokens) {
        queue = new LinkedList<>();

        for (int i = 0; i < tokens.size(); i++) {
            queue.add(tokens.get(i));
        }
        this.tokens = tokens;
    }

    /**
     * compare current token with expected token
     *
     * @param currentToken
     * @param expectedToken
     * @return boolean
     */
    public boolean match(Token.TokenType currentToken, Token.TokenType expectedToken) {
        if (currentToken == expectedToken) {
            return true;
        }
        return false;
    }


    /**
     * create main root of program and initialize parsing
     *
     * @return TreeNode
     */
    TreeNode program() {
        TreeNode program = new TreeNode("Program", "PROGRAM");
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        TreeNode z = program;
        TreeNode child = stmtSequence();
        if (child.getType() != "ERROR") {
            z.setChild(child);
        } else {
            ERROR = child;
        }
        if (!queue.isEmpty()){
            z = new TreeNode("Syntax error", "ERROR");
        }

        if (ERROR.getType() == "ERROR") {
            return ERROR;
        } else {
            return z;
        }
    }


    /**
     * stmt-sequence → statement { ;statement }
     *
     * @return TreeNode
     */
    TreeNode stmtSequence() {
        TreeNode temp;
        TreeNode sibling;
        TreeNode root = null;
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        temp = statement();

        if (temp.getType() != "ERROR") {
            root = temp;
        } else {
            ERROR = temp;
        }

        while (!queue.isEmpty() && queue.peek().getType() == Token.TokenType.SEMI_COLON) {
            queue.remove();
            sibling = statement();
            if(sibling.getType()!= "ERROR") {
                temp.siblings = sibling;
                temp = temp.siblings;
            }
            else {
                return sibling;
            }
        }

        if (ERROR.getType() == "ERROR") {
            return ERROR;
        } else {
            return root;
        }
    }


    /**
     * statament -> if-stmt | repeat-stmt | assign-stmt |read-stmt |  write-stmt
     *
     * @return TreeNode
     */
    public TreeNode statement() {
        TreeNode nodeTemp = null, errorCheckNode = null;
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");
        switch (queue.peek().getType()) {
            case IF: {
                errorCheckNode = iFStmt();
                if (errorCheckNode.getType() == "ERROR") {
                    ERROR = errorCheckNode;
                } else {
                    nodeTemp = errorCheckNode;
                }
                break;
            }
            case REPEAT: {
                errorCheckNode = repeatStmt();
                if (errorCheckNode.getType() == "ERROR") {
                    ERROR = errorCheckNode;
                } else {
                    nodeTemp = errorCheckNode;
                }

                break;
            }
            case READ: {
                errorCheckNode = readStmt();
                if (errorCheckNode.getType() == "ERROR") {
                    ERROR = errorCheckNode;
                } else {
                    nodeTemp = errorCheckNode;
                }
                break;
            }
            case WRITE: {
                errorCheckNode = writeStmt();
                if (errorCheckNode.getType() == "ERROR") {
                    ERROR = errorCheckNode;
                } else {
                    nodeTemp = errorCheckNode;
                }
                break;
            }
            case IDENTIFIER: {
                errorCheckNode = assignStmt();
                if (errorCheckNode.getType() == "ERROR") {
                    ERROR = errorCheckNode;
                } else {
                    nodeTemp = errorCheckNode;
                }
                break;
            }
            default:
                ERROR = new TreeNode("Syntax Error", "ERROR");
                break;
        }
        if (ERROR.getType() == "ERROR") {
            return ERROR;
        } else {
            return nodeTemp;
        }
    }


    /**
     * if -stmt → if exp then stmt-sequence end
     *
     * @return TreeNode
     */
    public TreeNode iFStmt() {
        TreeNode expNode = null, stmtSeqNode = null;
        //if
        TreeNode nodeTemp = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");


        if (!queue.isEmpty()) {
            queue.remove();
        }
        // enter with number
        expNode = exp();

        if (expNode.getType() != "ERROR") {
            nodeTemp.setChild(expNode);
        } else {
            ERROR = expNode;
        }

        if (match(queue.peek().getType(), Token.TokenType.THEN)) {
            if (!queue.isEmpty()) {
                queue.remove();
            }
            stmtSeqNode = stmtSequence();

            if (stmtSeqNode.getType() != "ERROR") {
                nodeTemp.setChild(stmtSeqNode);
            } else {
                ERROR = stmtSeqNode;
            }
        } else {
            ERROR = new TreeNode("Syntax Error", "ERROR");
        }

        if (!queue.isEmpty() && match(queue.peek().getType(), Token.TokenType.END)) {
            if (!queue.isEmpty()) {
                queue.remove();
            }
        } else {
            ERROR = new TreeNode("Syntax Error", "ERROR");
        }

        if (ERROR.getType() == "ERROR") {
            return ERROR;
        } else {
            return nodeTemp;
        }
    }


    /**
     * repeat->stmt-sequence until exp
     *
     * @return TreeNode
     */
    public TreeNode repeatStmt() {
        TreeNode repeatRoot = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");


        if (!queue.isEmpty()) {
            queue.remove();
        }

        TreeNode bodyNode = null, testNode = null;

        bodyNode = stmtSequence();

        if (bodyNode.getType() != "ERROR") {
            repeatRoot.setChild(bodyNode);
            if (match(queue.peek().getType(), Token.TokenType.UNTIL)) {
                if (!queue.isEmpty()) {
                    queue.remove();
                }
                // identifer expected
                testNode = exp();
                if (testNode.getType() != "ERROR") {
                    repeatRoot.setChild(testNode);
                } else {
                    // handle error if testNode = null or error
                    ERROR = testNode;
                }

            } else {
                // handle if next token is not until
                ERROR = new TreeNode("Syntax Error", "ERROR");
            }
        } else {
            //handle error if bodyNode = null or error
            ERROR = bodyNode;
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return repeatRoot;
    }


    /**
     * assignStmt -> Identifier := exp
     *
     * @return
     */
    public TreeNode assignStmt() {

        TreeNode assignRoot = new TreeNode("assign", "ASSIGN");
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");
        TreeNode expNode = null;

        if (match(queue.peek().getType(), Token.TokenType.IDENTIFIER)) {

            assignRoot.setValue(queue.peek().getValue());
            if (!queue.isEmpty()) {
                queue.remove();
            }
            if (match(queue.peek().getType(), Token.TokenType.ASSIGN)) {
                if (!queue.isEmpty()) {
                    queue.remove();
                }

                expNode = exp();

                if (expNode.getType() != "ERROR") {
                    assignRoot.setChild(expNode);
                } else {
                    //handle if exp returns error
                    ERROR = expNode;
                }

            } else {
                // next token of identifier is not an assign
                ERROR = new TreeNode("Syntax Error", "ERROR");
            }

        } else {
            //handle if next token is not an identifier
            ERROR = new TreeNode("Syntax Error", "ERROR");
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return assignRoot;
    }


    /**
     * read - > read identifer
     *
     * @return TreeNode
     */
    public TreeNode readStmt() {
        TreeNode readRoot = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");
        if (!queue.isEmpty()) {
            queue.remove();
        }

        if (!match(queue.peek().getType(), Token.TokenType.IDENTIFIER)) {
            ERROR = new TreeNode("Syntax Error", "ERROR");
        } else {
            readRoot.setChild(new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType())));
            if (!queue.isEmpty()) {
                queue.remove();
            }
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return readRoot;
    }


    /**
     * write -> write exp
     *
     * @return TreeNode
     */
    public TreeNode writeStmt() {
        TreeNode writeRoot = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        TreeNode expNode = null;
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        if (!queue.isEmpty()) {
            queue.remove();
        }
        // identifier expected
        expNode = exp();
        if (expNode.getType() != "ERROR") {
            writeRoot.setChild(expNode);
        } else {
            ERROR = expNode;
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return writeRoot;
    }


    //

    /**
     * exp -> simple-exp comparison-op simple-exp | simple-exp
     *
     * @return TreeNode
     */
    public TreeNode exp() {
        // identifer
        TreeNode nodeComp = new TreeNode("op", "comparsion");
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        // da5l bl identifer
        TreeNode nodeTemp = simpleexp();

        if (nodeTemp.getType() != "ERROR")
            nodeComp.setChild(nodeTemp);
        else ERROR = nodeTemp;

        // comparison operator
        if (!queue.isEmpty() && (queue.peek().getType() == Token.TokenType.EQUAL || queue.peek().getType() == Token.TokenType.LESSTHAN)) {
            nodeComp.setValue(queue.peek().getValue());
            nodeComp.setType(String.valueOf(queue.peek().getType()));

            nodeComp.setType(String.valueOf(queue.peek().getType()));
            if (!queue.isEmpty()) {
                queue.remove();
            }
            TreeNode nodeSimple = simpleexp();
            if (nodeSimple.getType() != "ERROR")
                nodeComp.setChild(nodeSimple);
            else ERROR = nodeSimple;
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return nodeComp;
    }


    /**
     * simple-exp -> term {addop term}
     *
     * @return TreeNode
     */
    public TreeNode simpleexp() {
        // identifier exits
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        TreeNode nodeTemp = term();


        while ((!queue.isEmpty()) && (queue.peek().getType() == Token.TokenType.PLUS || queue.peek().getType() == Token.TokenType.MINUS)) {
            TreeNode addopNode = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));

            if (nodeTemp.getType() != "ERROR")
                addopNode.setChild(nodeTemp);
            else ERROR = nodeTemp;

            if (!queue.isEmpty()) {
                queue.remove();
            }
            // Set the right child
            // identifer

            TreeNode nodeTerm = term();
            if (nodeTemp.getType() != "ERROR")
                addopNode.setChild(nodeTerm);

            else ERROR = nodeTerm;

            // Update nodeTemp to the new addopNode
            nodeTemp = addopNode;
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return nodeTemp;
    }


    /**
     * term -> factor {mulop factor}
     *
     * @return TreeNode
     */
    public TreeNode term() {
        TreeNode nodeTemp = factor();
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        while (((!queue.isEmpty()) && (queue.peek().getType() == Token.TokenType.MULT || queue.peek().getType() == Token.TokenType.DIV))) {
            // Create a new node for the mulop
            TreeNode mulopNode = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
            if (nodeTemp.getType() != "ERROR")
                mulopNode.setChild(nodeTemp); // Set the left child to the current factor
            else ERROR = nodeTemp;
            if (!queue.isEmpty()) {
                queue.remove();
            }
            TreeNode nodeFactor = factor();
            if (nodeTemp.getType() != "ERROR")
                mulopNode.setChild(nodeFactor);
            else ERROR = nodeFactor;
            nodeTemp = mulopNode;
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return nodeTemp;
    }


    /**
     * factor -> (exp) | number | identifier
     *
     * @return TreeNode
     */
    public TreeNode factor() {
        TreeNode nodeTemp = null;
        TreeNode ERROR = new TreeNode("FALSE", "FALSE");

        switch (queue.peek().getType()) {
            case OPENBRACKET: {
                if (!queue.isEmpty()) {
                    queue.remove();
                }
                TreeNode nodeEXP = exp();
                if (nodeEXP.getType() != "ERROR")
                    nodeTemp = nodeEXP;
                else
                    ERROR = nodeEXP;

                if (match(queue.peek().getType(), Token.TokenType.CLOSEDBRACKET)) {
                    if (!queue.isEmpty()) {
                        queue.remove();
                    }
                } else {
                    ERROR = new TreeNode("Syntax Error", "ERROR");
                }
                break;
            }
            case NUMBER:
            case IDENTIFIER: {
                nodeTemp = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
                if (!queue.isEmpty()) {
                    queue.remove();
                }
                break;
            }
            default: {
                ERROR = new TreeNode("Syntax Error", "ERROR");
                break;
            }
        }
        if (ERROR.getType() == "ERROR")
            return ERROR;
        else return nodeTemp;
    }
}