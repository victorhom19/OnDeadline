package com.victorhom;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import static com.victorhom.DateTimeUtilities.getDayOfWeek;
import static com.victorhom.FXMLUtilities.loadFXML;
import static com.victorhom.FXMLUtilities.setRoot;
import static com.victorhom.TMLUtilities.*;

import java.io.*;
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

    private Text todayTasks = new Text();

    private Text tomorrowTasks = new Text();

    private Text futureTasks = new Text();


    @FXML
    void closeApp() {
        System.exit(1);
    }

    @FXML
    void switchToNewTask() throws IOException {
        System.out.println("Boop");
        setRoot("NewTaskTab", App.scene);
    }


    @FXML
    void switchToEditTaskList() throws IOException {
        Pane mainPane = new Pane();
        Scene scene = new Scene(mainPane);
        mainPane.setPrefSize(300, 580);
        mainPane.setStyle("-fx-background-color: linear-gradient(#C64A48 0%, #FFF47C 100%)");

        ScrollPane sp = new ScrollPane();
        sp.setPrefSize(300,580);
        sp.setStyle("-fx-background-color: transparent; -fx-border-insets: 0; -fx-padding: 0");

        Pane content = new Pane();

        Text paneName = new Text("Редактировать список задач");
        paneName.setFont(new Font(19));
        paneName.setLayoutX(40);
        paneName.setY(32);
        content.getChildren().add(paneName);
        paneName.toFront();

        GridPane grid = new GridPane();
        grid.addRow(getTasks().size());
        int i = -1;
        for (String task : getTasks()) {
            i++;

            grid.setLayoutX(20);
            grid.setLayoutY(40);
            grid.getRowConstraints().add(new RowConstraints(100));
            grid.getColumnConstraints().add(new ColumnConstraints(170));


            Text text;
            if (getTaskType(task).equals("Regular")) {
                text = new Text(task + System.lineSeparator() + Arrays.toString(getTaskDays(task)) + System.lineSeparator() + getTaskTime(task));
            } else {
                text = new Text(task + System.lineSeparator() + Arrays.toString(getTaskDays(task)) + System.lineSeparator() + getTaskTime(task));
            }

            text.setFont(new Font(20));

            grid.getChildren().add(text);
            GridPane.setRowIndex(text, i);

            Button deleteButton = new Button();
            deleteButton.setPrefWidth(40);
            deleteButton.setPrefHeight(40);
            GridPane.setRowIndex(deleteButton, i);
            GridPane.setColumnIndex(deleteButton, 1);
            ImageView closeImage = new ImageView(getClass().getResource("/com/victorhom/images/close.png").toExternalForm());
            ImageView closeImageRed = new ImageView(getClass().getResource("/com/victorhom/images/close_red.png").toExternalForm());
            closeImage.setFitHeight(40);
            closeImage.setFitWidth(40);
            closeImageRed.setFitHeight(40);
            closeImageRed.setFitWidth(40);
            deleteButton.setStyle("-fx-background-color: transparent");
            deleteButton.setGraphic(closeImage);
            deleteButton.setOnAction(e -> {
                try {
                    removeTask(task);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                deleteButton.setGraphic(closeImageRed);

            });

            Button editButton = new Button();
            editButton.setPrefWidth(40);
            editButton.setPrefHeight(40);
            GridPane.setRowIndex(editButton, i);
            GridPane.setColumnIndex(editButton, 1);
            ImageView editImage = new ImageView(getClass().getResource("/com/victorhom/images/edit.png").toExternalForm());
            editImage.setFitWidth(40);
            editImage.setFitHeight(40);
            editButton.setStyle("-fx-background-color: transparent");
            editButton.setGraphic(editImage);
            editButton.setOnAction(e -> {
                try {
                    EditTaskController.taskToRemove = task;
                    setRoot("EditTask", App.scene);
                    App.mainStage.setScene(App.scene);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            });


            grid.getChildren().add(deleteButton);
            grid.getChildren().add(editButton);
            GridPane.setMargin(deleteButton, new Insets(0, 0, 0, 50));


        }
        content.getChildren().add(grid);

        int contentWidth = 100 * getTasks().size() + 60;
        if (contentWidth > 580) {
            content.setPrefSize(300, contentWidth);
        } else {
            content.setPrefSize(300,580);
        }

        content.setStyle("-fx-background-color: linear-gradient(#C64A48 0%, #FFF47C 100%)");
        sp.setContent(content);

        Button back = new Button();
        back.setOnAction(e -> {
            try {
                setRoot("MainMenu", App.scene);
                App.mainStage.setScene(App.scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        ImageView backImage = new ImageView(getClass().getResource("/com/victorhom/images/back.png").toExternalForm());
        backImage.setFitHeight(20);
        backImage.setFitWidth(20);
        back.setStyle("-fx-background-color: transparent");
        back.setGraphic(backImage);
        back.setLayoutX(5);
        back.setLayoutY(10);
        content.getChildren().add(back);

        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainPane.getChildren().add(sp);


        App.mainStage.setScene(scene);

    }


    @FXML
    Button switchToNewTaskButton;

    private void putNumberOfTasks() throws IOException {
        BufferedReader todayReader = new BufferedReader(new FileReader("src/main/resources/Data/TodayTasksData.txt"));
        BufferedReader tomorrowReader = new BufferedReader(new FileReader("src/main/resources/Data/TomorrowTasksData.txt"));
        List<Integer> todayTasksCounter = new ArrayList<>();
        List<Integer> tomorrowTasksCounter = new ArrayList<>();
        int futureTasksCounter = 0;
        for (int i = 0; i < 24; i++) {
            todayTasksCounter.add(0);
            tomorrowTasksCounter.add(0);
        }

        DateTime currentTime = new DateTime();
        for (String task : getTasks()) {
            int index = Integer.parseInt(getTaskTime(task).substring(0, 2));
            if (getTaskType(task).equals("Regular")) {
                for (String weekDay : getTaskDays(task)) {
                    if (getDayOfWeek(currentTime.getDayOfWeek()).equals(weekDay)) {
                        todayTasksCounter.set(index, todayTasksCounter.get(index) + 1);
                    } else if (getDayOfWeek(currentTime.plusDays(1).getDayOfWeek()).equals(weekDay)) {
                        tomorrowTasksCounter.set(index, tomorrowTasksCounter.get(index) + 1);
                    } else {
                        futureTasksCounter ++;
                    }
                }
            } else {
                if (new ArrayList(Arrays.asList(getTaskDays(task))).get(0).equals(currentTime.toDateTime().toString().substring(0, 10))) {
                    todayTasksCounter.set(index, todayTasksCounter.get(index) + 1);
                } else if (new ArrayList(Arrays.asList(getTaskDays(task))).get(0).equals(currentTime.plusDays(1).toDateTime().toString().substring(0, 10))) {
                    tomorrowTasksCounter.set(index, todayTasksCounter.get(index) + 1);
                }
            }

        }

        StringBuilder todayResult = new StringBuilder();
        StringBuilder tomorrowResult = new StringBuilder();
        StringBuilder futureResult = new StringBuilder();


        for (int i = 0; i < 24; i++) {
            int timeTasksCount = todayTasksCounter.get(i);
            if (timeTasksCount != 0) {
                todayResult.append(todayReader.readLine(), 0, 13).append(" ").append("    Запланировано задач: ").append(timeTasksCount).append(System.lineSeparator());
            } else {
                todayResult.append(todayReader.readLine(), 0, 13).append(System.lineSeparator());
            }


            timeTasksCount = tomorrowTasksCounter.get(i);
            if (timeTasksCount != 0) {
                tomorrowResult.append(tomorrowReader.readLine(), 0, 13).append(" ").append("    Запланировано задач: ").append(timeTasksCount).append(System.lineSeparator());
            } else {
                tomorrowResult.append(tomorrowReader.readLine(), 0, 13).append(System.lineSeparator());
            }
        }
        futureResult.append("Запланировано задач: ").append(futureTasksCounter);

        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Data/TodayTasksData.txt"));
        writer.write(todayResult.toString());
        writer.close();

        writer = new BufferedWriter(new FileWriter("src/main/resources/Data/TomorrowTasksData.txt"));
        writer.write(tomorrowResult.toString());
        writer.close();

        writer = new BufferedWriter(new FileWriter("src/main/resources/Data/FutureTasksData.txt"));
        writer.write(futureResult.toString());
        writer.close();
    }

    @FXML
    private void initialize() throws IOException {

        putNumberOfTasks();

        updateTimetable();

        timeMarker.start();


        for (Node node : gridPane.getChildren()) {
            node.setOnMouseClicked(e -> {
                Stage newStage = new Stage();
                EventHandler<MouseEvent> closingTab = mouseEvent -> {
                    newStage.close();
                };

                for (Node closingNode : gridPane.getChildren()) {
                    switchToNewTaskButton.addEventHandler(MouseEvent.MOUSE_CLICKED, closingTab);
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

    private void updateTimetable() throws IOException {
        todayTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/TodayTasksData.txt")));
        todayTasks.setFont(new Font(15));
        todayTasksList.getChildren().add(todayTasks);

        tomorrowTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/TomorrowTasksData.txt")));
        tomorrowTasks.setFont(new Font(15));
        tomorrowTasksList.getChildren().add(tomorrowTasks);

        futureTasks = new Text(Files.readString(Paths.get("src/main/resources/Data/FutureTasksData.txt")));
        futureTasks.setFont(new Font(15));
        futureTasksList.getChildren().add(futureTasks);
    }


    @FXML
    private GridPane gridPane;

    ScheduledService timeMarker = new ScheduledService() {
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



}
