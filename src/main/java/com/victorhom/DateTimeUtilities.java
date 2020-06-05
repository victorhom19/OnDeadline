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

    static int getDayOfWeek(String day) {
        switch (day) {
            case "пн":
                return 1;
            case "вт":
                return 2;
            case "ср":
                return 3;
            case "чт":
                return 4;
            case "пт":
                return 5;
            case "сб":
                return 6;
            case "вс":
                return 7;
            default:
                return 0;
        }
    }

}
