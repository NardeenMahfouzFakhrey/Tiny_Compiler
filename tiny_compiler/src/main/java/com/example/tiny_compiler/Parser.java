package com.example.tiny_compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;


public class Parser {

    ArrayList<Token> tokens = new ArrayList<>();
    int count = 0;
    int size = 0;
    TreeNode root;
    //Boolean flag1 = false;
    //Boolean flag2 = false;
    Queue<Token> queue;
    //TreeNode currentRoot = root;


    TreeNode program() {
         TreeNode program=new TreeNode("Program", "PROGRAM");

        TreeNode z= program;

        TreeNode child = stmtSequence();
        z.setChild(child);
        return z;
    }

    TreeNode stmtSequence() {
        TreeNode  temp;
        TreeNode sibling;

        temp = statement();

        TreeNode  root = temp;

            while (!queue.isEmpty() && queue.peek().getType() == Token.TokenType.SEMI_COLON) {
                queue.remove();
                sibling = statement();
                    temp.siblings=sibling;
                    temp = temp.siblings;

                }
        return root;
    }


    public Parser(ArrayList<Token> tokens) {
        queue = new LinkedList<>();

        for(int i=0 ; i <tokens.size() ; i++) {
            queue.add(tokens.get(i));
        }
        this.tokens = tokens;
    }
     /*
     *Done Testing
     */

    public boolean match(Token.TokenType currentToken, Token.TokenType expectedToken) {
        if (currentToken == expectedToken) {
            return true;
        }
        return false;
    }

    /*
     *Done Testing
     */

    public Token getNextToken() {
        count++;
        return tokens.get(count);
    }
    /*
     *Done Testing
     */
    //program→ stmt-sequence
//    public void program() {
//        System.out.println("starting");
//        root = new TreeNode("start", "START");
//        //currentRoot=root;
//        root.setChild(stmtSequence());
//
//        System.out.println("ending");
//    }
//
//    /*
//     *Done Testing
//     */
//    //stmt-sequence → stmt-sequence  ; statement |  statement
//
//    //stmt-sequence → statement { ;statement }
//
//    public TreeNode stmtSequence() {
//        TreeNode nodeTemp;
//        System.out.println("stmtSequence" + "token value " + queue.peek().getValue());
//        nodeTemp = statement();
//        if(!queue.isEmpty()){
//        while (!queue.isEmpty() && (queue.peek().getType() == Token.TokenType.SEMI_COLON )) {
//            System.out.println("while loop for stmt-seq "+ queue.peek().getValue());
//            queue.remove();
////            if (flag1==true) {
////                currentRoot=root;
////            }
////            if(flag2==true){
////                currentRoot=root;
////            }
////            //if assign(identifer) repeat read write
////            currentRoot.setChild(statement());
//         }
//        }
//        //what about handling error here
//        return nodeTemp;
//    }

    /*
     *Done Testing
     */
    //statement→if- stmt,repeat-stmt,assign-stmt,read-stmt,write-stmt
    public TreeNode statement() {
        TreeNode nodeTemp = null;
        switch (queue.peek().getType()) {
            case IF: {
                nodeTemp = iFStmt();

                break;
            }
            case REPEAT: {
                nodeTemp = repeatStmt();
                break;
            }
            case READ: {
                nodeTemp = readStmt();
                break;
            }
            case WRITE: {
                nodeTemp = writeStmt();
                break;
            }
            case IDENTIFIER: {
                nodeTemp = assignStmt();
                break;
            }
            default: //throw error or what to do ??
        }
        return nodeTemp;
    }



    //if -stmt → if exp then stmt-sequence end
    public TreeNode iFStmt() {
        //if
        TreeNode nodeTemp = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        //currentRoot=nodeTemp;
        System.out.println("if-stmt function "+ queue.peek().getValue());
         if(!queue.isEmpty()) {
         queue.remove();
         }
        // enter with number
        nodeTemp.setChild(exp());

        if (match(queue.peek().getType(), Token.TokenType.THEN )) {
            if(!queue.isEmpty()) {
                queue.remove();
            }
            nodeTemp.setChild(stmtSequence());
        }

        if (match(queue.peek().getType(), Token.TokenType.END)){
            if(!queue.isEmpty()) {
                queue.remove();
                //flag1=true;
            }
        }
        return nodeTemp;
    }

    /** errors to handle
     * 1- if count < size
     * 2- if any non-terminal function returns error
     * 3- if two tokens does not match
     * **/
    //repeat->stmt-sequence until exp
    public TreeNode repeatStmt() {
        TreeNode repeatRoot = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        //currentRoot=repeatRoot;
        if(!queue.isEmpty()) {
            queue.remove();
        }

        TreeNode bodyNode = null, testNode = null;

        bodyNode = stmtSequence();

        if (bodyNode != null) {
            repeatRoot.setChild(bodyNode);

            if (match(queue.peek().getType(), Token.TokenType.UNTIL)) {
                if(!queue.isEmpty()) {
                    queue.remove();
                }
                // identifer expected
                testNode = exp();
                if (testNode != null) {
                    repeatRoot.setChild(testNode);
                    //flag2 = true;
                } else {
                    // handle error if testNode = null or error
                }

            }
            else{
                // handle if next token is not until
            }
        }
        else {
            //handle error if bodyNode = null or error
        }
        return repeatRoot;
    }

     // assignStmt -> Identifier := exp
    public TreeNode assignStmt() {

        TreeNode assignRoot = new TreeNode("assign", "assign");

        TreeNode expNode = null;
        System.out.println(" peek 1" + queue.peek().getType());

        if(match(queue.peek().getType(),Token.TokenType.IDENTIFIER)){

             assignRoot.setValue(queue.peek().getValue());
             if(!queue.isEmpty()) {
                 queue.remove();
             }
            if(match(queue.peek().getType(),Token.TokenType.ASSIGN)){
                if(!queue.isEmpty()) {
                    queue.remove();
                }
                // identifer expected
                System.out.println("exp " + queue.peek().getValue());
                expNode=exp();

                if(expNode != null){
                    assignRoot.setChild(expNode);
                }
                else{
                    //handle if exp returns error
                }

            }
            else{
                // next token of identifier is not as assign
            }

        }
        else{
            //handle if next token is not an identifier
        }
        return assignRoot;
    }

    /*
     *Done Testing
     */
    // read - > read identifer
    public TreeNode readStmt() {
        TreeNode readRoot = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        if(!queue.isEmpty()) {
            queue.remove();
        }

        if(!match(queue.peek().getType(), Token.TokenType.IDENTIFIER)){
            //handle error next token is not an identifier
        }
        else {
            readRoot.setChild(new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType())));
            if(!queue.isEmpty()) {
                queue.remove();
            }
        }
        return readRoot;
    }


    // write -> write exp

    public TreeNode writeStmt() {
        TreeNode writeRoot =  new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
        TreeNode expNode = null;

        if(!queue.isEmpty()) {
            queue.remove();
        }
        // identifier expected
        expNode = exp();

        if(expNode != null){
            writeRoot.setChild(expNode);
        }
        else{
            //handle return error from exp
        }
        return writeRoot;
    }


    // exp -> simple-exp comparison-op simple-exp | simple-exp

    public TreeNode exp() {
        // identifer
        TreeNode nodeComp = new TreeNode("op" , "comparsion");
        // da5l bl identifer
        TreeNode nodeTemp = simpleexp();
        nodeComp.setChild(nodeTemp);

        // comparison operator
        if ( !queue.isEmpty() && (queue.peek().getType() == Token.TokenType.EQUAL || queue.peek().getType()== Token.TokenType.LESSTHAN)) {
            nodeComp.setValue(queue.peek().getValue());
            if(!queue.isEmpty()) {
                queue.remove();
            }
            nodeComp.setChild(simpleexp());
            return nodeComp;
        }
        return nodeTemp;
    }


    // simple-exp -> simple-exp addop term | term

    // simple-exp -> term {addop term}

    public TreeNode simpleexp() {
        // identifier exits
        TreeNode nodeTemp = term();

        while (( !queue.isEmpty()) && (queue.peek().getType() == Token.TokenType.PLUS || queue.peek().getType() == Token.TokenType.MINUS)) {
            TreeNode addopNode = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));

            addopNode.setChild(nodeTemp);

            if(!queue.isEmpty()) {
                queue.remove();
            }
            // Set the right child
            // identifer
            addopNode.setChild(term());
            // Update nodeTemp to the new addopNode
            nodeTemp = addopNode;
        }
        return nodeTemp;
    }


    // term -> term mulop factor | factor
    // term -> factor {mulop factor}

    public TreeNode term() {

        TreeNode nodeTemp = factor();

        while (((!queue.isEmpty()) && (queue.peek().getType() == Token.TokenType.MULT || queue.peek().getType() == Token.TokenType.DIV))) {

            // Create a new node for the mulop
            TreeNode mulopNode = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
            mulopNode.setChild(nodeTemp); // Set the left child to the current factor

            if(!queue.isEmpty()) {
                queue.remove();
            }
            mulopNode.setChild(factor());
            nodeTemp = mulopNode;
        }
        return nodeTemp;
    }

    // factor -> (exp) | number | identifier
    public TreeNode factor() {
        TreeNode nodeTemp = null;
        switch (queue.peek().getType()) {
            case OPENBRACKET: {
                if (count < size - 1) {
                    if(!queue.isEmpty()) {
                        queue.remove();
                    }
                    nodeTemp = exp();

                        if (match(queue.peek().getType(), Token.TokenType.CLOSEDBRACKET)) {
                            if(!queue.isEmpty()) {
                                queue.remove();
                            }
                        }
                        } else {


                        }
                break;
            }
            case NUMBER:
            case IDENTIFIER: {
                nodeTemp = new TreeNode(queue.peek().getValue(), String.valueOf(queue.peek().getType()));
                if(!queue.isEmpty()) {
                    queue.remove();
                }
                break;
            }
            default: {
                // Error: Invalid factor
                break;
            }
        }
        return nodeTemp;
    }
}
