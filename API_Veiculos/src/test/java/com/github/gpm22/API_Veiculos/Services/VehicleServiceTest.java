package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Clients.ApiFipe.ApiFipeClient;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Price;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Year;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Services.impl.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    private IVehicleService vehicleService = new VehicleService();

    @Mock
    private ApiFipeClient apiFipeClient;

    private void init() {
        MockitoAnnotations.openMocks(this);
        Brand[] brands = new Brand[]{new Brand("A", "1"), new Brand("B", "2")};
        Mockito.when(apiFipeClient.getBrandList("carros")).thenReturn(brands);
        Model[] models = new Model[]{new Model("B", "1"), new Model("A", "2")};
        Mockito.when(apiFipeClient.getModelList("carros", "1")).thenReturn(models);
        Year[] years = new Year[]{new Year("2005-2", "1"), new Year("2005-1", "2")};
        Mockito.when(apiFipeClient.getYearList("carros", "1", "1")).thenReturn(years);
    }

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

    @Test
    public void getFipePriceTestWrongThrow(){
        init();

        Vehicle nonExistentBrand = new Vehicle("C", "B", "2005-2", "carros");
        Assertions.assertThrows(IllegalArgumentException.class,  () -> vehicleService.getFipePrice(nonExistentBrand));

        Vehicle nonExistentModel = new Vehicle("A", "C", "2005-2", "carros");
        Assertions.assertThrows(IllegalArgumentException.class,  () -> vehicleService.getFipePrice(nonExistentModel));

        Vehicle nonExistenYear = new Vehicle("A", "B", "2005-3", "carros");
        Assertions.assertThrows(IllegalArgumentException.class,  () -> vehicleService.getFipePrice(nonExistenYear));
    }

    @Test
    public void getFipePriceTestCorrect(){
        init();
        Mockito.when(apiFipeClient.getFipePrice("carros", "1", "1", "1")).thenReturn(new Price("R$ 100.000,00"));
        Vehicle car = new Vehicle("A", "B", "2005-2", "carros");
        Assertions.assertEquals( "R$ 100.000,00", vehicleService.getFipePrice(car));
    }
}
