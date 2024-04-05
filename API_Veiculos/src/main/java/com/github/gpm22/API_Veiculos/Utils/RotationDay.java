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
        int yearLastDigit = Integer.parseInt(year.substring(3, 4));
        return rotationDay[yearLastDigit];
    }

}
