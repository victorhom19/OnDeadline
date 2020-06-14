package victorhom.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private String date;
    private ArrayList<Integer> weekdays = new ArrayList<>();

    static public ObservableList<String> weekDays = FXCollections.observableArrayList(
            "пн", "вт", "ср", "чт", "пт", "сб", "вс");

    public String getDate() {
        return date;
    }

    public ArrayList<Integer> getWeekdays() {
        return weekdays;
    }

    public String getWeekdaysAsString() {
        StringBuilder result = new StringBuilder();
        for (int day : weekdays) {
            result.append(weekDays.get(day)).append(", ");
        }
        return result.substring(0, result.length() - 2);
    }

    public Day(DateTime fullFormatDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
        date = formatter.print(fullFormatDate);
    }

    public Day(String formattedDate) {
        date = formattedDate;
    }

    public Day(List<Integer> weekDays) {
        weekdays.addAll(weekDays);
    }

    @Override
    public String toString() {
        return (date);
    }
}
