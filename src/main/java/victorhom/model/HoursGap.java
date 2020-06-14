package victorhom.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HoursGap {
    private int hourFrom;
    private int hourTo;

    public HoursGap(int from) {
        hourFrom = from;
        hourTo = from + 1;
    }

    public HoursGap(String formattedTime) {
        hourFrom = Integer.parseInt(formattedTime.substring(0,2));
        hourTo = Integer.parseInt(formattedTime.substring(8,10));
    }

    public int getGapIndex() {
        return (hourFrom);
    }


    @Override
    public String toString() {
        return (String.format("%02d:00 - %02d:00" , hourFrom, hourTo));
    }

    public static ObservableList<String> timePeriods = FXCollections.observableArrayList(
            "00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00", "03:00 - 04:00",
            "04:00 - 05:00", "05:00 - 06:00", "06:00 - 07:00", "07:00 - 08:00",
            "08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
            "12:00 - 13:00", "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00",
            "16:00 - 17:00", "17:00 - 18:00", "18:00 - 19:00", "19:00 - 20:00",
            "20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 24:00");
}
