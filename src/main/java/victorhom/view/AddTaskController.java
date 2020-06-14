package victorhom.view;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.CheckComboBox;
import victorhom.model.Day;
import victorhom.model.HoursGap;
import victorhom.model.Task;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import static victorhom.view.App.mainPlanner;
import static victorhom.view.FXMLUtilities.setRoot;


public class AddTaskController {

    private Task.TaskType taskType = Task.TaskType.Regular;

    @FXML
    private AnchorPane shortPane;

    @FXML
    private DatePicker dateChooser;

    @FXML
    private AnchorPane regularPane;

    @FXML
    private CheckComboBox<String> repeatDaysChooser;

    @FXML
    private TextField taskName;

    @FXML
    private ComboBox<String> timeChooser;

    @FXML
    private TextArea description;

    @FXML
    private void initialize() {
        timeChooser.setItems(HoursGap.timePeriods);
        timeChooser.setValue(HoursGap.timePeriods.get(0));

        repeatDaysChooser.getItems().addAll(Day.weekDays);
        repeatDaysChooser.show();

    }

    @FXML
    void back() throws IOException {
        setRoot("MainMenu", App.scene);
    }

    @FXML
    void createNewTask() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        if (taskType.equals(Task.TaskType.Regular)) {
            mainPlanner.addTask(taskName.getText(), taskType, new HoursGap(timeChooser.getValue()), new Day(repeatDaysChooser.getCheckModel().getCheckedIndices()), description.getText());
        } else {
            mainPlanner.addTask(taskName.getText(), taskType, new HoursGap(timeChooser.getValue()), new Day(formatter.format(dateChooser.getValue())), description.getText());
        }
        back();
    }


    @FXML
    void selectRegularType() {
        taskType = Task.TaskType.Regular;
        shortPane.toBack();
    }

    @FXML
    void selectShortType() {
        taskType = Task.TaskType.Short;
        regularPane.toBack();
    }




}
