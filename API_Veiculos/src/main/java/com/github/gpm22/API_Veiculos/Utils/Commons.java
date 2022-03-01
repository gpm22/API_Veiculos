package com.github.gpm22.API_Veiculos.Utils;

import java.util.Calendar;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Commons {

    final private static Map<String, Integer> rotationDay = Stream.of(new Object[][] {
            {"0", Calendar.MONDAY},
            {"1", Calendar.MONDAY},
            {"2", Calendar.TUESDAY},
            {"3", Calendar.TUESDAY},
            {"4", Calendar.WEDNESDAY},
            {"5", Calendar.WEDNESDAY},
            {"6", Calendar.THURSDAY},
            {"7", Calendar.THURSDAY},
            {"8", Calendar.FRIDAY},
            {"9", Calendar.FRIDAY},
    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

    public static Boolean isRotationActive(int rotationDay) {
        return rotationDay == Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public static int rotationDay(String year) {
        String lastDigit = year.substring(3, 4);
        return rotationDay.get(lastDigit);
    }

    public static Boolean cpfValidation(String cpf){
        return cpfValidateFormat(cpf) && cpfValidateCalculation(cpf);
    }

    private static Boolean cpfValidateFormat(String cpf) {
        String cpfValidation = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern.compile(cpfValidation).matcher(cpf).matches();
    }

    private static Boolean cpfValidateCalculation(String cpf) {
        int[] cpfDigits = Stream.of(cpf.replaceAll("[^0-9]", "").split("")).mapToInt(Integer::parseInt).toArray();
        return cpfDigitValidation(cpfDigits, 1) && cpfDigitValidation(cpfDigits, 2)
                && isAInvalidKnownCpf(cpfDigits);
    }

    private static boolean isAInvalidKnownCpf(int[] cpf) {

        for (int i = 1; i < cpf.length; i++) {
            if (cpf[i - 1] != cpf[i]) {
                return true;
            }
        }

        return false;
    }

    private static boolean cpfDigitValidation(int[] cpf, int digit) {

        int digitPosition;

        int comparisonValue;

        if (digit == 1) {
            digitPosition = 9;
        } else if (digit == 2) {
            digitPosition = 10;
        } else {
            throw new IllegalArgumentException("the value of digit can be only 1 and 2 and not " + digit);
        }

        int sum = 0;

        for (int i = 0; i < digitPosition; i++) {
            sum += cpf[i] * (digitPosition + 1 - i);
        }

        comparisonValue = ((sum * 10) % 11);

        if (comparisonValue == 10) {
            comparisonValue = 0;
        }

        return cpf[digitPosition] == comparisonValue;
    }

}
