module com.codefx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires core;


    exports com.codeFx.Controller;
    opens com.codeFx.Controller to javafx.fxml;
}