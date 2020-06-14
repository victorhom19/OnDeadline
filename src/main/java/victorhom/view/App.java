package victorhom.view;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import victorhom.model.DailyPlanner;

import java.io.IOException;
import java.sql.SQLException;

import static victorhom.view.FXMLUtilities.loadFXML;

public class App extends Application {

    static Scene scene;
    static DailyPlanner mainPlanner = new DailyPlanner();
    static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        mainPlanner.uploadTasksData();
        mainStage = new Stage();
        scene = new Scene(loadFXML("MainMenu"));
        mainStage.setScene(scene);
        configureStage(mainStage);
    }


    public static void main(String[] args) {
        launch();
    }


    private void configureStage(Stage mainStage) {
        mainStage.setResizable(false);
        mainStage.initStyle(StageStyle.UNDECORATED);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        mainStage.show();
        mainStage.setX(screenBounds.getWidth() - mainStage.getScene().getWidth());
        mainStage.setY(screenBounds.getHeight() - mainStage.getScene().getHeight());
    }

    @Override
    public void stop() throws SQLException {
        mainPlanner.saveTasksData();
    }
    
}
