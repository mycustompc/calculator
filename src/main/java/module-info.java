module ca.mycustompc.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires EvalEx;
    requires java.desktop;
    requires java.prefs;
    requires annotations;
    opens ca.mycustompc.calculator to javafx.fxml;
    exports ca.mycustompc.calculator;
}