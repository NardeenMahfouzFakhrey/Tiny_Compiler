package com.example.tiny_compiler;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;

public class SyntaxTreeGUI extends Pane {
    private static int NODE_WIDTH = 60;
    private static int NODE_HEIGHT = 40;
    private static int NODE_GAP = 15;

    String statement[] = {"IF", "REPEAT", "ASSIGN", "READ", "WRITE"};
    List<String> statements = Arrays.asList(statement);
    String valuesNode[] = {"LESSTHAN","IDENTIFIER","NUMBER","ASSIGN"};
    List<String> valuesNodes = Arrays.asList(valuesNode);
    String op[] = {"LESSTHAN","PLUS","MINUS","MULT","DIV","EQUAL"};
    List<String> ops = Arrays.asList(op);


    public void setSyntaxTree(TreeNode root) {
        clear();
        drawTree(root, 50, 50);
    }

    public void clear() {
        this.getChildren().clear();
    }

    void drawTree(TreeNode node, double xPos, double yPos) {
        
        if (node == null) {
            return;
        }
        if(node.value == "op"){
            drawTree(node.children.get(0),xPos,yPos);
        }else {

            if (node.type != "PROGRAM") {
                Color strokePaint;
                if (statements.contains(node.type)) {
                    strokePaint = Color.BLACK;
                    addRectangle(xPos, yPos, strokePaint);
                } else {
                    strokePaint = Color.DARKRED;
                    addEllipse(xPos, yPos, strokePaint);
                }

                addText(xPos, yPos, node, strokePaint);
            }

            double childX = xPos;
            double childY = yPos + 2 * NODE_HEIGHT;
            int numOfChildren = node.children.size();
            if (numOfChildren > 0) {
                double branchesGap = numOfChildren * (NODE_WIDTH + NODE_GAP) - NODE_GAP;
                childX -= 0.5 * (branchesGap - NODE_WIDTH);
                childX = ensureNotOverlapped(childX, childY);
            } else {
                childX += 2 * NODE_WIDTH;
            }

            //ArrayList<TreeNode> children = node.getChilds();
            for (int i = 0; i < node.children.size(); i++) {
                double linkXStart = xPos + 0.5 * NODE_WIDTH;
                double linkYStart = yPos + NODE_HEIGHT;
                if (node.type != "READ") {
                    if (node.type != "PROGRAM") {
                        if (node.type == "REPEAT") {
                            childX = ensureNotOverlapped(childX, childY);
                            Line line = new Line(linkXStart, linkYStart, childX + 0.5 * NODE_WIDTH, childY);
                            this.getChildren().add(line);
                        }else{
                            Line line = new Line(linkXStart, linkYStart, childX + 0.5 * NODE_WIDTH, childY);
                            this.getChildren().add(line);
                        }
                    }

                    drawTree(node.getChilds().get(i), childX, childY);
                }
                childX += getLevelWidth(node.children.get(i)) * (NODE_WIDTH + NODE_GAP);
            }


            // determine next tree coordinates
            double siblingX = childX + NODE_GAP;
            double siblingLinkY = yPos + 0.5 * NODE_HEIGHT;

            // add next tree
            TreeNode siblingTree = node.siblings;
            drawTree(siblingTree, siblingX, yPos);
            if (siblingTree != null) {
                Line line = new Line(xPos + NODE_WIDTH, siblingLinkY, siblingX, siblingLinkY);
                this.getChildren().add(line);
            }


            // adjust pane size
            double boundX = siblingX + getLevelWidth(siblingTree) * (NODE_WIDTH + NODE_GAP);
            adjustBounds(boundX, childY);
        }

    }
    private void addEllipse(double xPos, double yPos, Color strokePaint) {
        double centerX = xPos + 0.5*NODE_WIDTH;
        double centerY = yPos + 0.5*NODE_HEIGHT;

        Ellipse ellipse = new Ellipse(centerX, centerY, 0.5*NODE_WIDTH, 0.5*NODE_HEIGHT);
        ellipse.setStroke(strokePaint);
        ellipse.setFill(Color.TRANSPARENT);
        this.getChildren().add(ellipse);
    }

    private void addRectangle(double xPos, double yPos, Color strokePaint) {
        Rectangle rectangle = new Rectangle(xPos, yPos, NODE_WIDTH, NODE_HEIGHT);

        rectangle.setStroke(strokePaint);
        rectangle.setFill(Color.TRANSPARENT);
        this.getChildren().add(rectangle);
    }


    private void addText(double xPos, double yPos, TreeNode node, Color strokePaint){

        if(ops.contains(node.type)) {
            addTextOnNode(xPos, yPos + 15, "op", strokePaint);
        }else if(node.type == "IDENTIFIER"){
            addTextOnNode(xPos, yPos + 15, "id", strokePaint);
        }
        else if(node.type == "NUMBER"){
            addTextOnNode(xPos, yPos + 15, "const", strokePaint);
        }
        else if(node.type == "IF" || node.type == "REPEAT" || node.type == "WRITE") {
            addTextOnNode(xPos, yPos + 22.5, node.value, strokePaint);
        }else if (node.type == "ASSIGN"){
            addTextOnNode(xPos, yPos + 15, node.type.toLowerCase(), strokePaint);

        }else {
            addTextOnNode(xPos, yPos + 15, node.value, strokePaint);

        }

        if (node.type == "READ") {
            addTextOnNode(xPos, yPos + 30, "(" + node.children.get(0).getValue() + ")", strokePaint);
        }else if (valuesNodes.contains(node.type) || ops.contains(node.type)){
            addTextOnNode(xPos, yPos + 30, "(" + node.getValue() + ")", strokePaint);
        }
    }

    private void addTextOnNode(double xPos, double yPos, String text, Color strokePaint) {
        Text textObj = new Text(text);
        double labelXPos = xPos + 0.5 * (NODE_WIDTH-textObj.getLayoutBounds().getWidth());

        textObj.setX(labelXPos);
        textObj.setY(yPos);
        textObj.setStroke(strokePaint);
        this.getChildren().add(textObj);
    }

    public static int getLevelWidth(TreeNode root) {
        if (root == null)
            return 0;

        int width = 0;
        int numOfChildren = root.children.size();

        if (numOfChildren == 0) {
            if (root.siblings == null) {
                return 1;
            } else {
                width = 1;
            }
        }

        for (int i = 0; i < numOfChildren; i++) {
            width += getLevelWidth(root.children.get(i));
        }
        return width + getLevelWidth(root.siblings);
    }

    private void adjustBounds(double x, double y) {
        if (x > this.getWidth()) {
            double delta = x- this.getWidth();
            this.setMinWidth(this.getWidth() + delta);
        }

        if (y > this.getHeight()) {
            double delta = y- this.getHeight();
            this.setMinHeight(this.getHeight() + delta);
        }
    }

    private double ensureNotOverlapped(double x, double y) {
        for (Node node: this.getChildren()) {
            Shape shape;
            if (node instanceof Shape) {
                shape = (Shape) node;
            } else {
                continue;
            }

            if (x < 0)
                return NODE_GAP;
            if (shape.intersects(x, y, NODE_WIDTH, NODE_HEIGHT)) {
                if (shape instanceof Rectangle) {
                    Rectangle rect = (Rectangle) shape;
                    return rect.getX() + rect.getWidth() + NODE_GAP;
                } else if (shape instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse) shape;
                    return ellipse.getCenterX() + ellipse.getRadiusX() + NODE_GAP;
                } else {
                    return x + NODE_GAP;
                }
            }
        }
        return x;
    }
}