package ca.mycustompc.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/ca/mycustompc/calculator/calculator.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();

        Scene scene = new Scene(root, 300, 500);
        controller.setScene(scene);
        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setMinHeight(500);
        stage.setMinWidth(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}