package com.victorhom;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static com.victorhom.FXMLUtilities.loadFXML;
import static com.victorhom.FXMLUtilities.setRoot;
import static com.victorhom.TMLUtilities.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

public class MainMenuController {


    @FXML
    private TextFlow todayTasksList;

    @FXML
    private TextFlow tomorrowTasksList;

    @FXML
    private TextFlow futureTasksList;

    @FXML
    private Text todayTasks = new Text();

    @FXML
    private Text tomorrowTasks = new Text();

    @FXML
    private Text futureTasks = new Text();

    @FXML
    void switchToNewNote() throws IOException {
        System.out.println(findTag(Files.readString(Paths.get("src/main/resources/Data/Data.txt")), "fdsfsdf"));
    }

    @FXML
    void switchToNewTask() throws IOException {
        setRoot("newtask", App.scene);
    }

    @FXML
    void switchToEditTaskList() {

    }

    @FXML
    Button switchToNewTaskButton;


    @FXML
    private void initialize() throws IOException {
        todayTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/TodayTasksData.txt")));
        todayTasks.setFont(new Font(15));
        todayTasksList.getChildren().add(todayTasks);

        tomorrowTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/TomorrowTasksData.txt")));
        tomorrowTasks.setFont(new Font(15));
        tomorrowTasksList.getChildren().add(tomorrowTasks);

        futureTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/FutureTasksData.txt")));
        futureTasks.setFont(new Font(15));
        futureTasksList.getChildren().add(futureTasks);


        process.start();
        //shiftDays.setCycleCount(Timeline.INDEFINITE);
        //shiftDays.play();

        for (Node node : gridPane.getChildren()) {
            node.setOnMouseClicked(e -> {
                Stage newStage = new Stage();
                EventHandler<MouseEvent> closingTab = mouseEvent -> {
                    newStage.close();
                };

                for (Node closingNode : gridPane.getChildren()) {
                    closingNode.addEventHandler(MouseEvent.MOUSE_CLICKED, closingTab);
                }
                Pane bgPane = new Pane();
                bgPane.setPrefWidth(150);
                bgPane.setPrefHeight(300);
                bgPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #FFF47C, #C64A48);" +
                        "-fx-border-color: black");
                Scene scene = new Scene(bgPane);
                if (newStage.getStyle() != StageStyle.UNDECORATED) {
                    newStage.initStyle(StageStyle.UNDECORATED);
                }
                newStage.setScene(scene);
                newStage.show();

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
                System.out.print(boundsInScreen.getMinY() + bgPane.getHeight());
                System.out.println(" " + screenBounds.getHeight());
                if (boundsInScreen.getMinY() + bgPane.getHeight() > screenBounds.getHeight()) {
                    newStage.setX(boundsInScreen.getMinX() - bgPane.getWidth() - 1);
                    newStage.setY(screenBounds.getHeight() - bgPane.getHeight());
                } else {
                    newStage.setX(boundsInScreen.getMinX() - bgPane.getWidth() - 1);
                    newStage.setY(boundsInScreen.getMinY());
                }
                newStage.setResizable(false);



            });
        }

    }


    private void setUpSlot(Node node, Stage stage) {
        Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
        System.out.println(stage.getScene());
        if (boundsInScreen.getHeight() < stage.getScene().getHeight()) {
            stage.setX(boundsInScreen.getMinX() - 150);
            stage.setY(boundsInScreen.getMaxY() - stage.getScene().getHeight());
        } else {
            stage.setX(boundsInScreen.getMinX() - 150);
            stage.setY(boundsInScreen.getMinY());
        }
        if (stage.getStyle() != StageStyle.UNDECORATED) {
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.setResizable(false);
    }


    @FXML
    private GridPane gridPane;

    ScheduledService process = new ScheduledService() {
        @Override
        protected Task createTask() {
            return new Task() {
                @Override
                protected Void call() throws Exception {
                    DateTime currentTime = new DateTime();
                    int i = currentTime.getHourOfDay();
                    gridPane.getChildren().get(i).setStyle("-fx-border-color: gold; -fx-border-width: 2 0 2 0; -fx-border-style: solid");
                    DateTime nextHour = currentTime.plusHours(1).minusMinutes(currentTime.getMinuteOfHour()).minusSeconds(currentTime.getSecondOfMinute());
                    int secondsForSleep = Seconds.secondsBetween(currentTime, nextHour).getSeconds();
                    System.out.println(secondsForSleep);
                    TimeUnit.SECONDS.sleep(secondsForSleep);
                    gridPane.getChildren().get(i).setStyle("-fx-border-color: #364958; -fx-border-width: 1 0 0 0; -fx-border-style: dashed;");
                    return null;
                }
            };
        }
    };

    Timeline shiftDays = new Timeline(new KeyFrame(Duration.seconds(12), new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            todayTasks = tomorrowTasks;
            tomorrowTasks = new Text("");

            todayTasksList.getChildren().clear();
            todayTasksList.getChildren().add(todayTasks);
            tomorrowTasksList.getChildren().clear();
            tomorrowTasksList.getChildren().add(tomorrowTasks);
        }
    }));



}
