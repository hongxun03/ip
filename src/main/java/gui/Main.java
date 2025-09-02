package gui;

import java.io.IOException;

import chatbot.Bubbles;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Bubbles using FXML.
 */
public class Main extends Application {

    private Bubbles bubbles = new Bubbles("./data/Bubbles.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setMaxWidth(800);
            fxmlLoader.<MainWindow>getController().setDuke(bubbles); // inject the Bubbles instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
