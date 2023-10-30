package com.example.tiny_compiler;

import java.util.ArrayList;

public class Scanner {

    private static int i = 0;
    public Token getToken(String inputData ){

        Token token = null;

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
        if (inputData.charAt(i) == ';') {
            token = new Token(Token.TokenType.SEMI_COLON,String.valueOf(inputData.charAt(i++)));
        }

        return token;
    }


}
