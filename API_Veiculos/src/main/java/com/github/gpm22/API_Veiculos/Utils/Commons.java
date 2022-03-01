package com.github.gpm22.API_Veiculos.Utils;

import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    public static Boolean cpfValidation(String cpf){
        return cpfValidateFormat(cpf) && cpfValidateCalculation(cpf);
    }

    private static Boolean cpfValidateFormat(String cpf) {
        String cpfPattern = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern.compile(cpfPattern).matcher(cpf).matches();
    }

    private static Boolean cpfValidateCalculation(String cpf) {
        int[] cpfDigits = Stream.of(cpf.replaceAll("[^0-9]", "").split("")).mapToInt(Integer::parseInt).toArray();
        return cpfDigitValidation(cpfDigits, 1) && cpfDigitValidation(cpfDigits, 2)
                && !isAInvalidKnownCpf(cpfDigits);
    }

    private static boolean cpfDigitValidation(int[] cpf, int digit) {

        int digitPosition;

        if (digit == 1) {
            digitPosition = 9;
        } else if (digit == 2) {
            digitPosition = 10;
        } else {
            throw new IllegalArgumentException("the value of digit can be only 1 and 2 and not " + digit);
        }

        int cpfSum = 0;

        for (int i = 0; i < digitPosition; i++) {
            cpfSum += cpf[i] * (digitPosition + 1 - i);
        }

        int sumValidationValue = ((cpfSum * 10) % 11);

        if (sumValidationValue == 10) {
            sumValidationValue = 0;
        }

        return cpf[digitPosition] == sumValidationValue;
    }

    private static boolean isAInvalidKnownCpf(int[] cpf) {

        //verifying if the cpf is not compose of the same number,
        //per example 000.000.000-00 and 111.111.111-11.

        for (int i = 1; i < cpf.length; i++) {
            if (cpf[i - 1] != cpf[i]) {
                return false;
            }
        }

        return true;
    }
}
