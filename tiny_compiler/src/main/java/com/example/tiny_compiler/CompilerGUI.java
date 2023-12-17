package com.example.tiny_compiler;

        import javafx.application.Application;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.geometry.Pos;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextArea;
        import javafx.scene.layout.VBox;
        import javafx.scene.text.Font;
        import javafx.scene.text.FontWeight;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;
        import java.io.IOException;
        import java.util.ArrayList;

public class CompilerGUI extends Application {

    public String codeLines = "";
        @Override
    public void start(Stage stage) throws IOException {


        Label label;
        TextArea linesTextArea = new TextArea();
        Button scanButton = new Button("Scan");
        linesTextArea.setPrefHeight(100);
        stage.setTitle("Scanner");
        label = new Label("Browse text file or Write code");
        label.setFont(Font.font("Arial" , FontWeight.NORMAL , 20));
        ///
        Button button = new Button("Browse");

        EventHandler<ActionEvent> event1 = e -> {
            FileChooser filechooser = new FileChooser();
            File file;
            Compiler compiler = new Compiler();
            compiler.setInputData(null);
            file = filechooser.showOpenDialog(stage);
            if (file != null) {
                compiler.readFile(file.getAbsolutePath());
                try {
                    compiler.compile(compiler.getInputData());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }; button.setOnAction(event1);

        scanButton.setOnAction(action -> {
            Compiler compiler = new Compiler();
            //compiler.setInputData(null);
            codeLines = "";
            codeLines = linesTextArea.getText()+" ";
            compiler.setInputData(codeLines);
            try {
                 compiler.compile(compiler.getInputData());
                 parsing(compiler.getTokenStream());
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

    public void parsing(ArrayList<Token> treeNode) {

        Stage window = new Stage();
        window.setTitle("Syntax Tree");
        Parser parser = new Parser(treeNode);

        SyntaxTreeGUI syntaxTreeGUI = new SyntaxTreeGUI();
        syntaxTreeGUI.setSyntaxTree(parser.program()); // Set the syntax tree

        Scene scene = new Scene(syntaxTreeGUI, 1000, 600);
        window.setScene(scene);

        window.show();
    }


    public static void main(String[] args) throws Exception {

//        Compiler compiler = new Compiler();
//        compiler.startScanner();
//        compiler.compile(compiler.getInputData());
        launch();
    }
}