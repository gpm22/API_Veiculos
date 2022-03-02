package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;

import java.util.List;
import java.util.Set;

public interface IVehicleService {

    Vehicle addVehicleToOwner(Owner owner, Vehicle vehicle) throws IllegalArgumentException;

    void verifyVehicleInfo(Vehicle vehicle) throws IllegalArgumentException;

    public Set<Vehicle> getOwnerVehiclesByEmailOrCpf(String emailOuCpf);

    public List<Vehicle> updateVehiclesPrices();

    Vehicle getVehicleById(String vehicleId);
}
