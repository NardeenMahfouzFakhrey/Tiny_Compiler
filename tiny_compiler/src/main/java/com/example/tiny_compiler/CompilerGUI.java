package com.example.tiny_compiler;

        import javafx.application.Application;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

public class CompilerGUI extends Application {

    public String codeLines = "";
    //    @Override
//    public void start(Stage stage) throws IOException {
//
//
//        Label label;
//        TextArea linesTextArea = new TextArea();
//        Button scanButton = new Button("Scan");
//        linesTextArea.setPrefHeight(100);
//        stage.setTitle("Scanner");
//        label = new Label("Browse text file or Write code");
//        label.setFont(Font.font("Arial" , FontWeight.NORMAL , 20));
//        ///
//        Button button = new Button("Browse");
//
//        EventHandler<ActionEvent> event1 = e -> {
//            FileChooser filechooser = new FileChooser();
//            File file;
//            Compiler compiler = new Compiler();
//            compiler.setInputData(null);
//            file = filechooser.showOpenDialog(stage);
//            if (file != null) {
//                compiler.readFile(file.getAbsolutePath());
//                try {
//                    compiler.compile(compiler.getInputData());
//                } catch (Exception ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }; button.setOnAction(event1);
//
//        scanButton.setOnAction(action -> {
//            Compiler compiler = new Compiler();
//            //compiler.setInputData(null);
//            codeLines = "";
//            codeLines = linesTextArea.getText()+" ";
//            System.out.println(codeLines);
//            compiler.setInputData(codeLines);
//            try {
//                 compiler.compile(compiler.getInputData());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        ///
//        VBox vbox = new VBox(50,label ,button,linesTextArea,scanButton);
//        vbox.setAlignment(Pos.CENTER);
//        Scene scene = new Scene(vbox,550, 400);
//        stage.setScene(scene);
//        stage.show();
//    }
    public static TreeNode takeInput(){
        TreeNode node = new TreeNode("start", "START");
        TreeNode readNode = new TreeNode("read","READ");

        TreeNode id = new TreeNode("x","IDENTIFIER");
        readNode.setChild(id);
        TreeNode ifNode = new TreeNode("if","IF");
        TreeNode cond = new TreeNode("<","LESSTHAN");
        TreeNode cond1 = new TreeNode("5","NUMBER");
        TreeNode thenNode = new TreeNode("then","THEN");
        TreeNode assign = new TreeNode("x","ASSIGN");
        TreeNode op = new TreeNode("+","PLUS");
        TreeNode par1= new TreeNode("x","IDENTIFIER");
        TreeNode par2= new TreeNode("3","NUMBER");

        op.setChild(par1);
        op.setChild(par2);
        assign.setChild(op);
        thenNode.setChild(readNode);
        thenNode.setChild(assign);
        cond.setChild(id);
        cond.setChild(cond1);
        ifNode.setChild(cond);
        ifNode.setChild(thenNode);
        node.setChild(readNode);
        node.setChild(ifNode);
        node.setChild(readNode);
        return assign;
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Syntax Tree");

//        SyntaxTreeGUI syntaxTreeGUI = new SyntaxTreeGUI();
//        syntaxTreeGUI.setSyntaxTree(takeInput()); // Set the syntax tree

        Compiler compiler = new Compiler();
        compiler.setInputData("{ Sample program in TINY language – computes factorial\n" +
                "}\n" +
                "read x; {input an integer }\n" +
                "if 0 < x then { don’t compute if x <= 0 }\n" +
                "fact := 1;\n" +
                "repeat\n" +
                "fact := fact * x;\n" +
                "x := x - 1\n" +
                "until x = 0;\n" +
                "write fact { output factorial of x }\n" +
                "end ");

        compiler.compile(compiler.getInputData());
        Parser parser = new Parser(compiler.getTokenStream());

        SyntaxTreeGUI syntaxTreeGUI = new SyntaxTreeGUI();
        syntaxTreeGUI.setSyntaxTree(parser.program()); // Set the syntax tree

        Scene scene = new Scene(syntaxTreeGUI, 800, 800);
        stage.setScene(scene);

        // Ensure the pane is resized when the scene is shown
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            syntaxTreeGUI.setPrefWidth((Double) newValue);
            syntaxTreeGUI.setSyntaxTree(takeInput());
        });

        stage.show();

    }


    public static void main(String[] args) throws Exception {

//        Compiler compiler = new Compiler();
//        compiler.startScanner();
//        compiler.compile(compiler.getInputData());
        launch();
    }
}