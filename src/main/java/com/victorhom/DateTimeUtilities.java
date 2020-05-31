package com.victorhom;

public class DateTimeUtilities {
    static String getDayOfWeek(int number) {
        switch (number) {
            case 1:
                return "пн";
            case 2:
                return "вт";
            case 3:
                return "ср";
            case 4:
                return "чт";
            case 5:
                return "пт";
            case 6:
                return "сб";
            case 7:
                return "вс";
            default:
                return null;
        }
    }
}
