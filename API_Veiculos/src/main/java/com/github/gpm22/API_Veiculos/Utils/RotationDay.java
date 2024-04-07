package com.github.gpm22.API_Veiculos.Utils;

import java.util.Calendar;

public class RotationDay {

    final public static int[] rotationDay = {
            Calendar.MONDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.FRIDAY };

    public static Boolean isRotationDayActive(int rotationDay) {
        return rotationDay == Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int getRotationDay(String year) {
        // The year will be 4 digits, like 2004, or 6 chars, like 1992-1
        // So we use always the fourth digit
        int yearLastDigit = Character.getNumericValue((year.charAt(3)));
        return rotationDay[yearLastDigit];
    }

}
