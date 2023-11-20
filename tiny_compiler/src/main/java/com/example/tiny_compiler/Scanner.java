package com.example.tiny_compiler;

import java.util.ArrayList;

public class Scanner {

    private static int i = 0;
    private static int line_counter = 1;

    String[] Reserved = {"if", "then", "end", "repeat", "until", "read", "write"};

    public Token getToken(String inputData ){

        Token token = null;
        String s = "";


        if (i >= inputData.length()-1){
            token = new Token(Token.TokenType.EOS,"\0");
            return token;
        }
        while ((Character.isWhitespace(inputData.charAt(i)) || inputData.charAt(i) == '{' || inputData.charAt(i) == '\n') && i < inputData.length()-1) {
            if (inputData.charAt(i) == '{') {
                while (inputData.charAt(i) != '}') {
                    i++;
                    if(i >= inputData.length()-1){
                      break;
                    }
                }
                if (i >= inputData.length()-1) {
                    token = new Token(Token.TokenType.ERROR,"Missing right curly bracket '}' at line:" + line_counter);
                    return token;
                }
            } else if (inputData.charAt(i) == '\n'){
                    line_counter++;
            }
            i++;

        }
        if ( Character.isDigit(inputData.charAt(i)) ) {
            while(Character.isDigit(inputData.charAt(i))){
                s += inputData.charAt(i++);
            }
            token = new Token(Token.TokenType.NUMBER,s);
        }
        else if (Character.isAlphabetic(inputData.charAt(i))) {
            while(Character.isAlphabetic(inputData.charAt(i))){
                s += inputData.charAt(i++);
            }
            boolean isReserved = false;
            for (String reservedWord : Reserved) {
                if (s.equals(reservedWord)) {
                    isReserved = true;
                    break;
                }
            }
            if (isReserved) {
                token = new Token(Token.TokenType.valueOf(s.toUpperCase()),s);
            } else {
                token = new Token(Token.TokenType.IDENTIFIER, s);
            }
        }
        else if (inputData.charAt(i) == '}'){
            token = new Token(Token.TokenType.ERROR,String.valueOf("Missing left curly bracket '{' at line: " + line_counter));
        }
        else if (inputData.charAt(i) == ':' && inputData.charAt(i+1) == '=') {
            token = new Token(Token.TokenType.ASSIGN,":=");
            i+=2;
        }
        else if (inputData.charAt(i) == '+') {
            token = new Token(Token.TokenType.PLUS,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '/') {
            token = new Token(Token.TokenType.DIV,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '-') {
            token = new Token(Token.TokenType.MINUS,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '*') {
            token = new Token(Token.TokenType.MULT,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '=') {
            token = new Token(Token.TokenType.EQUAL,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '(') {
            token = new Token(Token.TokenType.OPENBRACKET,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == ')') {
            token = new Token(Token.TokenType.CLOSEDBRACKET,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == ';') {
            token = new Token(Token.TokenType.SEMI_COLON,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i) == '<') {
            token = new Token(Token.TokenType.LESSTHAN,String.valueOf(inputData.charAt(i++)));
        }
        else if (inputData.charAt(i-1) == '}'){
            token = new Token(Token.TokenType.Comment,String.valueOf("Comment"));
        }
        else{
            token = new Token(Token.TokenType.ERROR,String.valueOf("There is an ERROR at line: " + line_counter));
        }
        return token;
    }
}
