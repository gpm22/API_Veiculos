package com.github.gpm22.API_Veiculos.Utils;

import java.util.Calendar;

public class Commons {
    public static Boolean isRotationActive(int rotationDay) {
        return rotationDay == Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int rotationDay(String year) {
        String lastDigit = year.substring(3, 4);

        return switch (lastDigit) {
            case "0", "1" -> Calendar.MONDAY;
            case "2", "3" -> Calendar.TUESDAY;
            case "4", "5" -> Calendar.WEDNESDAY;
            case "6", "7" -> Calendar.THURSDAY;
            case "8", "9" -> Calendar.FRIDAY;
            default -> -1;
        };
    }
}
