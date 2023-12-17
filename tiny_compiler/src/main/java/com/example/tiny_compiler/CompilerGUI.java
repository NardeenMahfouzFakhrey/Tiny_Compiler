package com.example.tiny_compiler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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


        EventHandler<ActionEvent> event1 = e -> {
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

                Compiler compiler = new Compiler();
                compiler.setInputData(null);

                compiler.readFile(file.getAbsolutePath());
                try {
                    compiler.compile(compiler.getInputData());
                    showParsing(compiler.getTokenStream());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        button.setOnAction(event1);

        scanButton.setOnAction(action -> {
            Compiler compiler = new Compiler();
            codeLines = "";
            if(linesTextArea.getText() == ""){
                showAlert("Error in Tiny Compiler","TextBox is empty");
            }else{
                codeLines = linesTextArea.getText() + " ";
                compiler.setInputData(codeLines);
                try {
                    compiler.compile(compiler.getInputData());
                    showParsing(compiler.getTokenStream());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        ///
        VBox vbox = new VBox(20, label, button, linesTextArea, scanButton);
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

    public void showParsing(ArrayList<Token> treeNode) {
        Parser parser = new Parser(treeNode);
        TreeNode root = parser.program();
        if (root.childs.get(0).getType() == "ERROR"){
            showAlert("Error in Tiny Compiler", root.childs.get(0).getValue());
            return;
        }
        syntaxTreeGUI.setSyntaxTree(root);
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}