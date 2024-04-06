package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;

import java.util.Collection;
import java.util.List;

public interface IVehicleService {

    Vehicle saveOrUpdateVehicle(Vehicle vehicle) throws IllegalArgumentException;

    int deleteVehiclesWithoutOwners(Vehicle vehicle);

    void verifyIfVehicleAlreadyExists(Vehicle vehicle);

    String getFipePrice(Vehicle vehicle);

    void verifyVehicleInfo(Vehicle vehicle) throws IllegalArgumentException;

    Collection<Vehicle> getAllVehicles();

    int updateVehiclesPrices();

    Vehicle getVehicleById(long vehicleId);
}
