module org.example.assignment13_multiplebouncingballs {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.assignment13_multiplebouncingballs to javafx.fxml;
    exports org.example.assignment13_multiplebouncingballs;
}