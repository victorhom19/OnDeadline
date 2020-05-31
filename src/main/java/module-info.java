module abcs {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.joda.time;

    opens com.victorhom to javafx.fxml;
    exports com.victorhom;
}