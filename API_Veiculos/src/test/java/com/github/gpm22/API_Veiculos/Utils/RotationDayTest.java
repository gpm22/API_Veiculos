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
        Assertions.assertEquals(RotationDay.getRotationDay("1991-5"), Calendar.MONDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("1802-4"), Calendar.TUESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("8093"), Calendar.TUESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("2394"), Calendar.WEDNESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("9455-7"), Calendar.WEDNESDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("1956-A"), Calendar.THURSDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("9457-5"), Calendar.THURSDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("5578"), Calendar.FRIDAY);
        Assertions.assertEquals(RotationDay.getRotationDay("2579"), Calendar.FRIDAY);
    }
}
