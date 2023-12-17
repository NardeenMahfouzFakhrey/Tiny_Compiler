package com.example.tiny_compiler;

import java.util.ArrayList;

public class TreeNode {
    public String value;
    public String type;
    TreeNode nextNode = null;
    public ArrayList<TreeNode> childs= new ArrayList<>();
    TreeNode siblings;


    public TreeNode(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<TreeNode> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<TreeNode> childs) {
        this.childs = childs;
    }

    public void setChild(TreeNode child) {
        this.childs.add(child);
    }


}
