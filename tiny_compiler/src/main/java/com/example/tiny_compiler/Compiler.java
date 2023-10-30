package com.example.tiny_compiler;

import java.io.*;
import java.util.ArrayList;



public class Compiler {
    private ArrayList<Token> tokenStream = new ArrayList<>();
    private Scanner scanner = new Scanner();
    private String inputData;

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public void compile(String inputData){
        //call scanner with inputData to scan function to get all tokens one by one

    }

    public void setTokenStream(ArrayList<Token> tokenStream) {
        this.tokenStream = tokenStream;
    }

    public void savedToFile(){
        try {
        FileOutputStream fos = new FileOutputStream("OutputFiles\\output1.txt");
        String tokenLine = "";
        for(int i = 0; i < tokenStream.size(); i++){
            Token token = tokenStream.get(i);
            if(i == tokenStream.size()-1)
                tokenLine = token.getValue() +", "+ token.getType();
            else
                tokenLine = token.getValue() + ", " + token.getType() +"\n";
            fos.write(tokenLine.getBytes());
        }
        fos.flush();
        fos.close();
            ProcessBuilder pb = new ProcessBuilder("notepad.exe", "OutputFiles\\output.txt");
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public void readInputData(String input){
        input = input.replaceAll("\n"," ");
        setInputData(input);
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
                System.out.println("Enter your Code Lines: \nEnter (Enter then Ctrl+D to finish)");
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
