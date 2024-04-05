package com.github.gpm22.API_Veiculos.Utils;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import java.util.Calendar;
import java.util.Collection;

public class Commons {

    final private static int[] rotationDay = {
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

    public static Boolean isRotationActive(int rotationDay) {
        return rotationDay == Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int rotationDay(String year) {
        int yearLastDigit = Integer.parseInt(year.substring(3, 4));
        return rotationDay[yearLastDigit];
    }

    public static void updateVehiclesRotationActive(Collection<Vehicle> vehicles){
        vehicles.forEach(n -> n.setRotationActive(isRotationActive(n.getRotationDay())));
    }

    public static ResponseEntity<String> errorResponse(Exception e, HttpStatus httpStatus, String erroMessage, Logger logger){
        logger.error(erroMessage);
        e.printStackTrace();
        return ResponseEntity
                .status(httpStatus)
                .body(e.getMessage());
    }
}
