package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VehicleServiceTest {

    @Autowired
    private IVehicleService vehicleService;

    @Test
    public void verifyVehicleInfoTestPass(){
        Vehicle car = new Vehicle("A", "B", "2005-2", "carros");
        vehicleService.verifyVehicleInfo(car);
        Vehicle motorcycle = new Vehicle("To", "B56677", "2034", "motos");
        vehicleService.verifyVehicleInfo(motorcycle);
        Vehicle truck = new Vehicle("A", "B", "9230", "caminhoes");
        vehicleService.verifyVehicleInfo(truck);
        Vehicle fipe = new Vehicle("A", "B", "2432", "fipe");
        vehicleService.verifyVehicleInfo(fipe);
    }


    @Test
    public void verifyVehicleInfoTestThrow() {
        Vehicle withoutBrand = new Vehicle("", "B", "2005-2", "carros");
        Assertions.assertThrows(IllegalArgumentException.class, () -> vehicleService.verifyVehicleInfo(withoutBrand));
        Vehicle withoutModel = new Vehicle("To", "", "2034", "motos");
        Assertions.assertThrows(IllegalArgumentException.class, () -> vehicleService.verifyVehicleInfo(withoutModel));
        Vehicle withoutYear = new Vehicle("A", "B", "", "caminhoes");
        Assertions.assertThrows(IllegalArgumentException.class, () -> vehicleService.verifyVehicleInfo(withoutYear));
        Vehicle withoutType = new Vehicle("A", "B", "2432", "");
        Assertions.assertThrows(IllegalArgumentException.class, () -> vehicleService.verifyVehicleInfo(withoutType));
        Vehicle wrongType = new Vehicle("A", "B", "2432", "navio");
        Assertions.assertThrows(IllegalArgumentException.class, () -> vehicleService.verifyVehicleInfo(wrongType));
    }

}
