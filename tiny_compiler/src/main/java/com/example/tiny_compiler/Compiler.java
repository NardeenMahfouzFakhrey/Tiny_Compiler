package com.example.tiny_compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Compiler {
    private ArrayList<Token> tokenStream = new ArrayList<>();
    private Scanner scanner = new Scanner();
    private String inputData;

    public void compile(String inputData){
        //call scanner with inputData to scan function to get all tokens one by one

    }


    public void savedToFile(){
        //save the token stream to a file
    }

    public void readFile(String filePath){
        StringBuilder builder = new StringBuilder();

        try (BufferedReader buffer = new BufferedReader(
                new FileReader(filePath))) {

            String str;

            while ((str = buffer.readLine()) != null) {

                builder.append(str).append("\n");
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
         readInputData(String.valueOf(builder));
    }


    public String readInputData(String input){
        input = input.replaceAll("\n","");
        System.out.println(input);
        return input;
    }

    public void startScanner(){
        System.out.println("Enter F to scan a file or C to write your code lines: ");
        java.util.Scanner scan = new java.util.Scanner(System.in);
        String option = scan.nextLine();

        switch (option){
            case "F":
                System.out.println("Enter your file path: ");
                String filePath = scan.nextLine();
                readFile(filePath);
                break;
            case "C":
                System.out.println("Enter your Code Lines: \nEnter (Ctrl+D/Ctrl+Z to finish)");
                StringBuilder userInput = new StringBuilder();
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    userInput.append(line).append("\n");
                }
                readInputData(String.valueOf(userInput));
                break;
            default:
                System.out.println("Wrong Input Please try again");
                startScanner();
        }
    }


}
