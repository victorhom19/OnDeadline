package victorhom.view;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import victorhom.model.Day;
import victorhom.model.HoursGap;
import victorhom.model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static victorhom.view.App.mainPlanner;
import static victorhom.view.App.mainStage;
import static victorhom.view.FXMLUtilities.setRoot;

public class MainMenuController {


    @FXML
    private void initialize() {
        updateVisibleTaskTable();
        timeMarker.start();
        configureInfoTabs(gridToday);
        configureInfoTabs(gridTomorrow);
        configureInfoTabs(gridFuture);

    }


    @FXML
    private Button switchToNewTaskButton;

    @FXML
    private Button switchToEditTaskListButton;

    @FXML
    private Button closeAppButton;

    @FXML
    private TextFlow todayTasksList;

    @FXML
    private TextFlow tomorrowTasksList;

    @FXML
    private TextFlow futureTasksList;

    @FXML
    private GridPane gridToday;

    @FXML
    private GridPane gridTomorrow;

    @FXML
    private GridPane gridFuture;

    @FXML
    void closeApp() {
        mainStage.close();
    }

    @FXML
    void switchToEditTaskList() {
        EditTaskListController.initialize();
    }

    @FXML
    void switchToNewTask() throws IOException {
        setRoot("NewTaskTab", App.scene);
    }

    private void updateVisibleTaskTable() {

        List<Integer> todayTasksCounter = new ArrayList<>();
        List<Integer> tomorrowTasksCounter = new ArrayList<>();
        int futureTasksCounter = 0;
        for (int i = 0; i < 24; i++) {
            todayTasksCounter.add(0);
            tomorrowTasksCounter.add(0);
        }

        DateTime currentTime = new DateTime();
        for (Task task : mainPlanner.tasksPool) {
            if (task.getTaskType().equals(Task.TaskType.Regular)) {
                boolean today = task.getTaskDays().getWeekdays().contains(currentTime.getDayOfWeek() - 1);
                boolean tomorrow = task.getTaskDays().getWeekdays().contains(currentTime.plusDays(1).getDayOfWeek() - 1);
                if (today || tomorrow) {
                    if (today) {
                        todayTasksCounter.set(task.getTaskTime().getGapIndex(), todayTasksCounter.get(task.getTaskTime().getGapIndex()) + 1);
                    }
                    if (tomorrow) {
                        tomorrowTasksCounter.set(task.getTaskTime().getGapIndex(), tomorrowTasksCounter.get(task.getTaskTime().getGapIndex()) + 1);
                    }
                }
                futureTasksCounter++;
            } else {
                if (new Day(currentTime).getDate().equals(task.getTaskDays().getDate())) {
                    todayTasksCounter.set(task.getTaskTime().getGapIndex(), todayTasksCounter.get(task.getTaskTime().getGapIndex()) + 1);
                } else if (new Day(currentTime.plusDays(1)).getDate().equals(task.getTaskDays().getDate())) {
                    tomorrowTasksCounter.set(task.getTaskTime().getGapIndex(), tomorrowTasksCounter.get(task.getTaskTime().getGapIndex()) + 1);
                } else {
                    futureTasksCounter++;
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < HoursGap.timePeriods.size(); i++) {
            builder.append(HoursGap.timePeriods.get(i));
            if (todayTasksCounter.get(i) != 0) {
                builder.append("\tЗапланировано задач: ").append(todayTasksCounter.get(i));
            }
            builder.append(System.lineSeparator());
        }
        Text text = new Text(builder.toString());
        text.setFont(new Font(15));
        todayTasksList.getChildren().clear();
        todayTasksList.getChildren().add(text);


        builder = new StringBuilder();
        for (int i = 0; i < HoursGap.timePeriods.size(); i++) {
            builder.append(HoursGap.timePeriods.get(i));
            if (tomorrowTasksCounter.get(i) != 0) {
                builder.append("\tЗапланировано задач: ").append(tomorrowTasksCounter.get(i));
            }
            builder.append(System.lineSeparator());
        }
        text = new Text(builder.toString());
        text.setFont(new Font(15));
        tomorrowTasksList.getChildren().clear();
        tomorrowTasksList.getChildren().add(text);

        text = new Text("Запланировано задач: " + futureTasksCounter);
        text.setFont(new Font(15));
        futureTasksList.getChildren().clear();
        futureTasksList.getChildren().add(text);

    }

    private ScheduledService timeMarker = new ScheduledService() {
        @Override
        protected javafx.concurrent.Task createTask() {
            return new javafx.concurrent.Task() {
                @Override
                protected Void call() throws Exception {
                    Platform.runLater(MainMenuController.this::updateVisibleTaskTable);
                    DateTime currentTime = new DateTime();
                    int i = currentTime.getHourOfDay();
                    gridToday.getChildren().get(i).setStyle("-fx-border-color: gold; -fx-border-width: 2 0 2 0; -fx-border-style: solid");
                    DateTime nextHour = currentTime.plusHours(1).minusMinutes(currentTime.getMinuteOfHour()).minusSeconds(currentTime.getSecondOfMinute());
                    int secondsForSleep = Seconds.secondsBetween(currentTime, nextHour).getSeconds();
                    TimeUnit.SECONDS.sleep(secondsForSleep);
                    gridToday.getChildren().get(i).setStyle("-fx-border-color: #364958; -fx-border-width: 1 0 0 0; -fx-border-style: dashed;");
                    return null;
                }
            };
        }
    };

    private void configureInfoTabs(GridPane dataGrid) {
        for (Node node : dataGrid.getChildren()) {
            node.setOnMouseClicked(e -> {
                Stage newStage = new Stage();
                EventHandler<MouseEvent> closingTab = mouseEvent -> Platform.runLater(newStage::close);

                switchToEditTaskListButton.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);
                switchToNewTaskButton.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);
                closeAppButton.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);

                for (Node closingNode : gridToday.getChildren()) {
                    closingNode.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);
                }
                for (Node closingNode : gridTomorrow.getChildren()) {
                    closingNode.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);
                }
                for (Node closingNode : gridFuture.getChildren()) {
                    closingNode.addEventHandler(MouseEvent.MOUSE_PRESSED, closingTab);
                }

                Pane bgPane = new Pane();
                bgPane.setPrefWidth(200);
                bgPane.setPrefHeight(400);
                bgPane.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #FFF47C, #C64A48);" +
                        "-fx-border-color: black");

                Text paneName;
                if (dataGrid.equals(gridFuture)) {
                    paneName = new Text("Предстоить выполнить:");
                } else {
                    paneName = new Text("Задачи на " + HoursGap.timePeriods.get(dataGrid.getChildren().indexOf(node)));
                }

                paneName.setFont(new Font(15));
                paneName.setLayoutX(10);
                paneName.setLayoutY(20);
                bgPane.getChildren().add(paneName);

                Button hideButton = new Button();
                hideButton.addEventHandler(MouseEvent.MOUSE_CLICKED, closingTab);
                ImageView hideImage = new ImageView(MainMenuController.class.getResource("/victorhom/view/images/eye.png").toExternalForm());
                hideImage.setFitHeight(20);
                hideImage.setFitWidth(20);
                hideButton.setStyle("-fx-background-color: transparent");
                hideButton.setGraphic(hideImage);
                hideButton.setLayoutX(170);
                bgPane.getChildren().add(hideButton);

                List<Task> tasksToVisualize;
                if (!dataGrid.equals(gridFuture)) {
                    tasksToVisualize = mainPlanner.findTasksByTime(dataGrid.getChildren().indexOf(node));
                } else {
                    tasksToVisualize = mainPlanner.findTasksByType(Task.TaskType.Regular);
                }

                ScrollPane sp = new ScrollPane();
                sp.setPrefSize(200, 400);
                sp.setStyle("-fx-background-color: transparent; -fx-border-insets: 0; -fx-padding: 0");
                sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                bgPane.getChildren().add(sp);

                Pane content = new Pane();
                content.getChildren().add(paneName);
                content.getChildren().add(hideButton);

                int totalLines = 0;

                if (!tasksToVisualize.isEmpty()) {

                    GridPane grid = new GridPane();
                    grid.addRow(mainPlanner.tasksPool.size());
                    grid.setLayoutY(40);

                    int i = -1;
                    for (Task task : tasksToVisualize) {
                        i++;
                        totalLines = 3 + countLines(task.getTaskDescription());

                        StringBuilder taskInfo = new StringBuilder();
                        taskInfo.append("Задача: ").append(task.getTaskName()).append(System.lineSeparator());
                        taskInfo.append("Дни: ");
                        if (task.getTaskType().equals(Task.TaskType.Regular)) {
                            taskInfo.append(task.getTaskDays().getWeekdaysAsString());
                        } else {
                            taskInfo.append(task.getTaskDays().getDate());
                        }
                        taskInfo.append(System.lineSeparator());
                        taskInfo.append("Время: ").append(task.getTaskTime().toString()).append(System.lineSeparator());
                        taskInfo.append("Описание:").append(System.lineSeparator()).append(task.getTaskDescription());


                        Text text = new Text(taskInfo.toString());
                        text.setFont(new Font(14));
                        GridPane.setMargin(text, new Insets(0, 0, 10, 5));

                        grid.getChildren().add(text);
                        GridPane.setRowIndex(text, i);

                    }
                    content.getChildren().add(grid);

                }

                int contentWidth = 80 * totalLines + 60;
                content.setPrefSize(200, Math.max(contentWidth, 400));

                content.setStyle("-fx-background-color: linear-gradient(#C64A48 0%, #FFF47C 100%)");
                sp.setContent(content);



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

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

}

