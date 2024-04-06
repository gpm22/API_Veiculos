package com.github.gpm22.API_Veiculos.Utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CPFValidatorTest {

    @Test
    public void invalidKnownCPFs(){
        for(int i = 0; i < 10 ; i++){
            String cpf = createInvalidKnownCPF(i);
            Assertions.assertFalse(CPFValidator.validateCPF(cpf));
            cpf = cpf.replace(".", "");
            Assertions.assertFalse(CPFValidator.validateCPF(cpf));
            cpf = cpf.replace("-", "");
            Assertions.assertFalse(CPFValidator.validateCPF(cpf));
        }
    }

    private String createInvalidKnownCPF(int i){
        return String.format("%d%<d%<d.%<d%<d%<d.%<d%<d%<d-%<d%<d", i);
    }

    @Test
    public void wrongCPFs() {
        String[] cpfs = new String[]{"11676455044", "81555116067", "51555241015", "41388872073", "46653920080"};
        for(String cpf: cpfs)
            Assertions.assertFalse(CPFValidator.validateCPF(cpf));
    }

    @Test
    public void wrongStrings(){
        Assertions.assertFalse(CPFValidator.validateCPF(""));
        Assertions.assertFalse(CPFValidator.validateCPF("abc"));
        Assertions.assertFalse(CPFValidator.validateCPF("12434523"));
    }

    @Test
    public void validCPFs() {
        String[] cpfs = new String[]{"12676455044", "81545116067", "51545241015", "41318872073", "46753920080"};
        for(String cpf: cpfs)
            testValidCPFVarieties(cpf);
    }

    private void testValidCPFVarieties(String cpf){

        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,3) + "." + cpf.substring(3,6)+"."+cpf.substring(6,9)+"-"+cpf.substring(9)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,6)+"."+cpf.substring(6,9)+"-"+cpf.substring(9)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,3) + "." + cpf.substring(3,9)+"-"+cpf.substring(9)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,3) + "." + cpf.substring(3,6)+"."+cpf.substring(6)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,9)+"-"+cpf.substring(9)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,3) + "." + cpf.substring(3)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf.substring(0,6)+"."+cpf.substring(6)));
        Assertions.assertTrue(CPFValidator.validateCPF(cpf));
    }
}
