package com.example.tiny_compiler;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class CompilerGUI extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}