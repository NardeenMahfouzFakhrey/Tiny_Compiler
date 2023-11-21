package com.example.tiny_compiler;

import java.io.*;
import java.util.ArrayList;

import static com.example.tiny_compiler.Token.TokenType.*;


public class Compiler {
    private ArrayList<Token> tokenStream = new ArrayList<>();
    private Scanner scanner = new Scanner();
    private String inputData;


    public ArrayList<Token> getTokenStream() {
        return tokenStream;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public void compile(String inputData) throws Exception {
        //call scanner with inputData to scan function to get all tokens one by one
        scan();
    }

    public ArrayList<Token> scan() throws Exception {

        while (true) {
            Token token = scanner.getToken(inputData);

            if (token.getType() == EOS ){
                savedToFile();
                scanner.setI(0);
                break;
            }
            else if ( token.getType() == ERROR){
                //throw new Exception(token.getValue());
                tokenStream.add(token);
                savedToFile();
                scanner.setI(0);
                break;
            }
            tokenStream.add(token);
        }
//        for (Token token : tokenStream){
//            System.out.println("Type: "+token.getType()+", value: "+token.getValue());
//        }
         return tokenStream;
    }

    public void setTokenStream(ArrayList<Token> tokenStream) {
        this.tokenStream = tokenStream;
    }

    public void savedToFile(){
        try {
        String basePath = System.getProperty("user.dir");

        String filePath = basePath + File.separator + "tiny_compiler" + File.separator + "OutputFiles" + File.separator + "output.txt";

        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Create directories if they don't exist

        FileOutputStream fos = new FileOutputStream(file);

        String tokenLine = "";
        if (tokenStream.get(tokenStream.size()-1).getType() == ERROR ){
            fos.write(tokenStream.get(tokenStream.size()-1).getValue().getBytes());
        }
        else {
            for (int i = 0; i < tokenStream.size(); i++) {
                Token token = tokenStream.get(i);
                if (token.getType() == Comment){
                    continue;
                }
                if (i == tokenStream.size() - 1)
                    tokenLine = token.getValue() + ", " + token.getType();
                else
                    tokenLine = token.getValue() + ", " + token.getType() + "\n";
                fos.write(tokenLine.getBytes());
            }
        }
        fos.flush();
        fos.close();
            ProcessBuilder pb = new ProcessBuilder("notepad.exe",filePath );
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
        //input = input.replaceAll("\n"," ");
       // System.out.println(input);
        setInputData(input);
    }

    public void startScanner(){
        System.out.println("Enter F to scan a file or C to write your code lines: ");
        java.util.Scanner scan = new java.util.Scanner(System.in);
        String option = scan.nextLine();

        switch (option){
            case "F","f":
                System.out.println("Enter your file path: ");
                String filePath = scan.nextLine();
                readFile(filePath);
                break;
            case "C","c":
                System.out.println("Enter your Code Lines: \nPress (Double Enter) to finish");
                StringBuilder userInput = new StringBuilder();
                java.util.Scanner s = new java.util.Scanner(System.in);
                String line;
                while(true){
                    line = s.nextLine();
                    if(line.equals("")){
                        break;
                    }
                    else {
                        //System.out.println(line);
                        userInput.append(line).append("\n");
                    }
                }
                readInputData(String.valueOf(userInput));
                System.out.println(line);
                break;
            default:
                System.out.println("Wrong Input Please try again");
                startScanner();
        }
    }


}
