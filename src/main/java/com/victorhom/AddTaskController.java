package com.victorhom;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import java.io.*;
import java.util.List;
import java.util.StringJoiner;

import static com.victorhom.FXMLUtilities.setRoot;
import static com.victorhom.TMLUtilities.*;

public class AddTaskController {

    private enum TaskType {
        regularType,
        shortType;

        @Override
        public String toString() {
            switch (this) {
                case regularType:
                    return "Regular";
                case shortType:
                    return "Short";
            }
        return null;
        }
    }

    private TaskType taskType = TaskType.regularType;;

    @FXML
    private AnchorPane regularPane;

    @FXML
    private AnchorPane shortPane;

    @FXML
    private void initialize() {
        timeChooser.setItems(timePeriods);
        timeChooser.setValue(timePeriods.get(0));

        repeatDaysChooser.getItems().addAll(weekDays);
        repeatDaysChooser.show();

        taskType = TaskType.regularType;
    }

    @FXML
    private void back() throws IOException {
        setRoot("MainMenu1", App.scene);
    }

    @FXML
    void selectRegularType(MouseEvent event) {
        taskType = TaskType.regularType;
        shortPane.toBack();
    }

    @FXML
    void selectShortType(MouseEvent event) {
        taskType = TaskType.shortType;
        regularPane.toBack();
    }



    @FXML
    void createNewTask() throws IOException {
        String type = taskType.toString();
        StringBuilder result = new StringBuilder();
        String namePart = taskName.getText();
        String typePart = type;
        String timePart = timeChooser.getValue();
        String descriptionPart = description.getText();
        String daysPart;
        if (taskType.equals(TaskType.regularType)) {
            daysPart = "";
            for (Object day : repeatDaysChooser.getCheckModel().getCheckedItems()) {
                daysPart += day.toString() + ", ";
            }
            daysPart = daysPart.substring(0 , daysPart.length() - 2);
        } else {
            daysPart = dateChooser.getValue().toString();
        }
        result.append(addParentTag((addChildTag(namePart, "taskName") + addChildTag(typePart, "taskType") + addChildTag(timePart, "time") + addChildTag(daysPart, "days") + addParentTag(descriptionPart, "description")), "task"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Data/Data.txt", true));
        writer.write(result.toString());
        writer.close();


    }



    @FXML
    private ComboBox<String> timeChooser;

    ObservableList<String> timePeriods = FXCollections.observableArrayList(
            "00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00", "03:00 - 04:00",
            "04:00 - 05:00", "05:00 - 06:00", "06:00 - 07:00", "07:00 - 08:00",
            "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
            "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
            "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00",
            "20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 24:00");

    @FXML
    private CheckComboBox<String> repeatDaysChooser;

    ObservableList<String> weekDays = FXCollections.observableArrayList(
            "пн", "вт", "ср", "чт", "пт", "сб", "вс");

    @FXML
    private TextField taskName;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker dateChooser;
}
