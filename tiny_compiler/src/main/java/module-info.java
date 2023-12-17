module com.example.tiny_compiler {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.tiny_compiler to javafx.fxml;
    exports com.example.tiny_compiler;
}