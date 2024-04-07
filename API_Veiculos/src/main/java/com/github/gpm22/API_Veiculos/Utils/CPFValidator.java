package com.github.gpm22.API_Veiculos.Utils;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CPFValidator {

    public static String formatCPF(String cpf){

        String newCPF = cpf.replaceAll("[^\\d]", "");

        if(newCPF.length() != 11)
            throw new IllegalArgumentException("CPF must contain 11 digits");

        return newCPF.substring(0,3) + "."
            + newCPF.substring(3,6) + "."
            + newCPF.substring(6, 9) + "-"
            + newCPF.substring(9);
    }

    public static Boolean validateCPF(String cpf){
        return validateCPFFormat(cpf) && validateCPFCalculation(cpf);
    }

    private static Boolean validateCPFFormat(String cpf) {
        String cpfPattern = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern.compile(cpfPattern).matcher(cpf).matches();
    }

    private static Boolean validateCPFCalculation(String cpf) {
        int[] cpfDigits = Stream.of(cpf.replaceAll("[^0-9]", "").split("")).mapToInt(Integer::parseInt).toArray();
        return validateCPFDigit(cpfDigits, 1) && validateCPFDigit(cpfDigits, 2)
                && !isAnInvalidKnownCpf(cpfDigits);
    }

    private static boolean validateCPFDigit(int[] cpf, int digit) {

        int digitPosition;

        if (digit == 1) {
            digitPosition = 9;
        } else if (digit == 2) {
            digitPosition = 10;
        } else {
            throw new IllegalArgumentException("the value of digit can be only 1 and 2 and not " + digit);
        }

        int cpfSum = 0;

        for (int i = 0; i < digitPosition; i++)
            cpfSum += cpf[i] * (digitPosition + 1 - i);

        int sumValidationValue = ((cpfSum * 10) % 11);

        if (sumValidationValue == 10)
            sumValidationValue = 0;

        return cpf[digitPosition] == sumValidationValue;
    }

    private static boolean isAnInvalidKnownCpf(int[] cpf) {

        //verifying if the cpf is not composed of the same number,
        //per example 000.000.000-00 and 111.111.111-11.

        for (int i = 1; i < cpf.length; i++)
            if (cpf[i - 1] != cpf[i])
                return false;

        return true;
    }
}
