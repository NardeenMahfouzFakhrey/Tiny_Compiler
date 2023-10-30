package com.example.tiny_compiler;

import java.util.ArrayList;

public class Scanner {

    private static int i = 0;

    String[] Reserved = {"if", "then", "end", "repeat", "until", "read", "write"};

    public Token getToken(String inputData ){

        Token token = null;
        String s = null;

        while (Character.isWhitespace(inputData.charAt(i)) || inputData.charAt(i) == '{') {
            if (inputData.charAt(i) == '{') {
                while (inputData.charAt(i) != '}') {
                    i++;
                }
                if (i >= inputData.length()) {
                    token = new Token(Token.TokenType.ERROR,"Missing right curly bracket '}'");
                    return token;
                }
            }
            i++;
        }
        if (i >= inputData.length()){
            token = new Token(Token.TokenType.EOS,"\0");
            return token;
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
                s = s.toUpperCase();
                token = new Token(Token.TokenType.valueOf(s),s);
            } else {
                token = new Token(Token.TokenType.IDENTIFIER, s);
            }
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
        else{
            token = new Token(Token.TokenType.ERROR,String.valueOf(inputData.charAt(i)));
        }
        return token;
    }
}
