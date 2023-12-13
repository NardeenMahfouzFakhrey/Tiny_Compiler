package com.example.tiny_compiler;

import java.util.ArrayList;


public class Parser {

    ArrayList<Token> tokens = new ArrayList<>();
    int count = 0;
    int size=0;
    TreeNode root;

    public Parser(ArrayList<Token> tokens) {
        size= tokens.size();
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
        Token nextToken=null;

        nodeTemp=statement(token);

        if(count < size) {
            nextToken = getNextToken();
        }

       else
        {
         //throw error missing semicolumn
        }

       while(nextToken.getType() == Token.TokenType.SEMI_COLON && count < size) {
             nextToken =getNextToken();
             nodeTemp.setChild(statement(nextToken));
             //return from statmenet with what token to handle these??
        }

        //what about handling error here
        return nodeTemp;
    }


    //if -stmt → if exp then stmt-sequence {else stmt-sequence} end
    public TreeNode iFStmt(Token token) {
        TreeNode nodeTemp = new TreeNode(token.getValue(), String.valueOf(token.getType()));
        Token nextToken=null;

        //value of current token advance what you need
        nodeTemp.setChild(exp(token));

        if(count<size) {
            nextToken = getNextToken();
        }
        else {
             //error missing then statement
        }

        if (!match(nextToken.getType(), Token.TokenType.THEN)) {
            //throwerror
        }

        nodeTemp.setChild(stmtSequence(nextToken));


        //advance input or already advanced
         if(count<size) {
             nextToken = getNextToken();
         }
         else {
             //missing end of if
         }

        if (nextToken.getType() == Token.TokenType.ELSE) {
            nodeTemp.setChild(stmtSequence(nextToken));
        }

        else if(nextToken.getType() == Token.TokenType.END) {
             return nodeTemp;
        }
        else  {
            //error missing end
        }

        nextToken = getNextToken();
        if (!match(nextToken.getType(), Token.TokenType.END)) {
            //throwerror missing end
        }

        return nodeTemp;
    }

    public TreeNode assignStmt(Token token) {
        return null;
    }

    public TreeNode exp(Token token) {
        return null;
    }

    public TreeNode repeatStmt(Token token) {
        return null;
    }

    public TreeNode readStmt(Token token) {
        return null;
    }

    public TreeNode writeStmt(Token token) {
        return null;
    }

}
