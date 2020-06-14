package victorhom.view;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import victorhom.model.Task;

import java.io.IOException;

import static victorhom.view.App.mainPlanner;

import static victorhom.view.FXMLUtilities.setRoot;

class EditTaskListController {

    static void initialize() {
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
        grid.addRow(mainPlanner.tasksPool.size());
        int i = -1;
        for (Task task : mainPlanner.tasksPool) {
            i++;

            grid.setLayoutX(20);
            grid.setLayoutY(40);
            grid.getRowConstraints().add(new RowConstraints(100));
            grid.getColumnConstraints().add(new ColumnConstraints(170));

            Text text;
            if (task.getTaskType().equals(Task.TaskType.Short)) {
                text = new Text(task.getTaskName() + System.lineSeparator() + task.getTaskDays().getDate() + System.lineSeparator() + task.getTaskTime().toString());
            } else {
                text = new Text(task.getTaskName() + System.lineSeparator() + task.getTaskDays().getWeekdaysAsString() + System.lineSeparator() + task.getTaskTime().toString());
            }

            text.setFont(new Font(20));

            grid.getChildren().add(text);
            GridPane.setRowIndex(text, i);

            Button deleteButton = new Button();
            deleteButton.setPrefWidth(40);
            deleteButton.setPrefHeight(40);
            GridPane.setRowIndex(deleteButton, i);
            GridPane.setColumnIndex(deleteButton, 1);
            ImageView closeImage = new ImageView(EditTaskListController.class.getResource("/victorhom/view/images/close.png").toExternalForm());
            ImageView closeImageRed = new ImageView(EditTaskListController.class.getResource("/victorhom/view/images/close_red.png").toExternalForm());
            closeImage.setFitHeight(40);
            closeImage.setFitWidth(40);
            closeImageRed.setFitHeight(40);
            closeImageRed.setFitWidth(40);
            deleteButton.setStyle("-fx-background-color: transparent");
            deleteButton.setGraphic(closeImage);
            deleteButton.setOnAction(e -> {
                mainPlanner.removeTask(task.getTaskName());
                deleteButton.setGraphic(closeImageRed);

            });

            Button editButton = new Button();
            editButton.setPrefWidth(40);
            editButton.setPrefHeight(40);
            GridPane.setRowIndex(editButton, i);
            GridPane.setColumnIndex(editButton, 1);
            ImageView editImage = new ImageView(EditTaskListController.class.getResource("/victorhom/view/images/edit.png").toExternalForm());
            editImage.setFitWidth(40);
            editImage.setFitHeight(40);
            editButton.setStyle("-fx-background-color: transparent");
            editButton.setGraphic(editImage);
            editButton.setOnAction(e -> {
                try {
                    EditTaskController.taskToEdit = task;
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

        int contentWidth = 100 * mainPlanner.tasksPool.size() + 60;
        content.setPrefSize(300, Math.max(contentWidth, 580));

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

        ImageView backImage = new ImageView(EditTaskListController.class.getResource("/victorhom/view/images/back.png").toExternalForm());
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


}
