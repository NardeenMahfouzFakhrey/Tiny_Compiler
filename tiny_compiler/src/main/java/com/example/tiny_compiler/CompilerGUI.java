package com.example.tiny_compiler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompilerGUI extends Application {
    File file;
    public String codeLines = "";
    @Override
    public void start(Stage stage) throws IOException {
        Compiler compiler = new Compiler();

        Label label;
        TextArea linesTextArea = new TextArea();
        Button scanButton = new Button("Scan");
        linesTextArea.setPrefHeight(150);
        stage.setTitle("Scanner");
        label = new Label("Browse text file or Write code");
        label.setFont(Font.font("Arial" , FontWeight.NORMAL , 20));
        ///
        Button button = new Button("Browse");
        FileChooser filechooser = new FileChooser();
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                file = filechooser.showOpenDialog(stage);
                if (file != null) {
                    compiler.readFile(file.getAbsolutePath());
                    try {
                        compiler.compile(compiler.getInputData());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }; button.setOnAction(event1);

        scanButton.setOnAction(action -> {
            codeLines = linesTextArea.getText();
            codeLines = String.valueOf(codeLines)+" ";
            compiler.setInputData(codeLines);
            try {
                compiler.compile(compiler.getInputData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        ///
        VBox vbox = new VBox(50,label ,button,linesTextArea,scanButton);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox,550, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {

//        Compiler compiler = new Compiler();
//        compiler.startScanner();
//        compiler.compile(compiler.getInputData());
        launch();
    }
}