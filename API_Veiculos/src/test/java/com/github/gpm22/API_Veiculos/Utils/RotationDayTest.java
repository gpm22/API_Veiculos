package com.github.gpm22.API_Veiculos.Utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
public class RotationDayTest {

    @Test
    public void getRotationDayTest(){
        Assertions.assertEquals(RotationDay.getRotationDay("2000"), Calendar.MONDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("1991"), Calendar.MONDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("18092"), Calendar.TUESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("8093"), Calendar.TUESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("94"), Calendar.WEDNESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("9455"), Calendar.WEDNESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("956"), Calendar.THURSDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("94557"), Calendar.THURSDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("945578"), Calendar.FRIDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("579"), Calendar.FRIDAY);
    }
}
