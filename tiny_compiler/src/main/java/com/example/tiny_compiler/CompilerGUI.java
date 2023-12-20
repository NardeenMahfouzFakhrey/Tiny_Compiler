package com.example.tiny_compiler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.tiny_compiler.Token.TokenType.ERROR;

public class CompilerGUI extends Application {

    public String codeLines = "";
    private SyntaxTreeGUI syntaxTreeGUI;

    @Override
    public void start(Stage stage) throws IOException {

        Label label;
        TextArea linesTextArea = new TextArea();
        Button scanButton = new Button("Scan");
        scanButton.setStyle("-fx-font-size: 14;");  // Adjust font size

        linesTextArea.setPrefHeight(300);  // Adjusted height for the ScrollPane
        stage.setTitle("Tiny Compiler");

        // Adjusted styling for the label
        label = new Label("Browse text file or Write code");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextFill(Color.BLACK);
        ///
        Button button = new Button("Browse");
        button.setStyle("-fx-font-size: 14;");  // Adjust font size

        RadioButton codeRadioButton = new RadioButton("Input is Code");
        codeRadioButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        codeRadioButton.setTextFill(Color.BLACK);

        RadioButton tokenStreamRadioButton = new RadioButton("Input is Token Stream");
        tokenStreamRadioButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        tokenStreamRadioButton.setTextFill(Color.BLACK);

        ToggleGroup inputToggleGroup = new ToggleGroup();
        codeRadioButton.setToggleGroup(inputToggleGroup);
        tokenStreamRadioButton.setToggleGroup(inputToggleGroup);




        EventHandler<ActionEvent> event1 = e -> {
            RadioButton selectedRadioButton = (RadioButton) inputToggleGroup.getSelectedToggle();

            if (selectedRadioButton == null) {
                showAlert("Error in Tiny Compiler", "Please select one of the input options.");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a file");

            // Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    showAlert("Invalid File", "Please choose a file with a .txt extension.");
                    return;
                }
                linesTextArea.clear();
                if (selectedRadioButton == codeRadioButton){
                    Compiler compiler = new Compiler();
                    compiler.setInputData(null);
                    compiler.readFile(file.getAbsolutePath());
                    try {
                        compiler.compile(compiler.getInputData());
                        showParsing(compiler.getTokenStream());
                    } catch (Exception ex) {
                        showAlert("Error in Tiny Compiler", "Syntax Error");
                    }
                }
                else {
                    String tokensString = readFileAndReturnString(file.getAbsolutePath());
                    ArrayList<Token> tokenStream = parseTokenString(tokensString);
                    try {
                        showParsing(tokenStream);
                    } catch (Exception E) {
                        showAlert("Error in Tiny Compiler", "Syntax Error");
                    }
                }

            }
        };
        button.setOnAction(event1);

        scanButton.setOnAction(action -> {
            RadioButton selectedRadioButton = (RadioButton) inputToggleGroup.getSelectedToggle();

            if (selectedRadioButton == null) {
                showAlert("Error in Tiny Compiler", "Please select one of the input options.");
                return;
            }
            codeLines = "";
            if(linesTextArea.getText() == ""){
                showAlert("Error in Tiny Compiler","TextBox is empty");
            }else{
                codeLines = linesTextArea.getText().replaceAll("[\\s\\r\\n]+$", "") + " ";
               if (selectedRadioButton == codeRadioButton){
                   Compiler compiler = new Compiler();
                   compiler.setInputData(codeLines);
                   try {
                       compiler.compile(compiler.getInputData());
                       showParsing(compiler.getTokenStream());
                   } catch (Exception e) {
                       showAlert("Error in Tiny Compiler", "Syntax Error");
                   }
               }else {
                     ArrayList<Token> tokenStream = parseTokenString(codeLines);
                   try {
                       showParsing(tokenStream);
                   } catch (Exception e) {
                       showAlert("Error in Tiny Compiler", "Syntax Error");
                   }
               }
            }
        });
        ///
        HBox radioButtonsHBox = new HBox(10, codeRadioButton, tokenStreamRadioButton);
        radioButtonsHBox.setAlignment(Pos.CENTER_LEFT);
        radioButtonsHBox.setPadding(new Insets(15));
        radioButtonsHBox.setSpacing(40);
        VBox vbox = new VBox(20, radioButtonsHBox, label, button, linesTextArea, scanButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Set the minimum and maximum width for the VBox
        vbox.setMinWidth(150);
        vbox.setMaxWidth(400);

        syntaxTreeGUI = new SyntaxTreeGUI();
        syntaxTreeGUI.setPadding(new Insets(0,0,30,0));
        ScrollPane scrollPane = new ScrollPane(syntaxTreeGUI);
        scrollPane.setMinHeight(syntaxTreeGUI.getHeight());
        scrollPane.setMinWidth(syntaxTreeGUI.getMinWidth());

        BorderPane root = new BorderPane();
        root.setLeft(vbox);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1300, 600);
        stage.setScene(scene);
        stage.show();
    }
    private void showAlert(String title, String content) {
        syntaxTreeGUI.clear();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static ArrayList<Token> parseTokenString(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\n");

        for (String line : lines) {
            String[] parts = line.trim().split(",\\s*");
            if (parts.length == 2) {
                String value = parts[0].trim();
                String typeString = parts[1].trim();
                tokens.add(new Token(Token.TokenType.valueOf(typeString.toUpperCase()),value));
            }
        }

        return tokens;
    }
    public String readFileAndReturnString(String filePath) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader buffer = new BufferedReader(new FileReader(filePath))) {
            String str;

            while ((str = buffer.readLine()) != null) {
                builder.append(str).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return String.valueOf(builder);
    }

    public void showParsing(ArrayList<Token> treeNode) {
        if (treeNode.get(treeNode.size()-1).getType() == ERROR ){
            showAlert("Error in Tiny Compiler",treeNode.get(treeNode.size()-1).getValue());
        }else{
            Parser parser = new Parser(treeNode);
            TreeNode root = parser.program();
            if (root.getType() == "ERROR"){
                showAlert("Error in Tiny Compiler", root.getValue());
                return;
            }
            syntaxTreeGUI.setSyntaxTree(root);
        }
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}