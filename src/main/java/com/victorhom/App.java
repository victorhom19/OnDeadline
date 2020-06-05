package com.victorhom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import static com.victorhom.FXMLUtilities.loadFXML;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = new Stage();

        scene = new Scene(loadFXML("MainMenu"));

        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.show();

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.setX(screenBounds.getWidth() - scene.getWidth());
        mainStage.setY(screenBounds.getHeight() - scene.getHeight());

    }

    public static void main(String[] args) {
        launch();
    }

}

