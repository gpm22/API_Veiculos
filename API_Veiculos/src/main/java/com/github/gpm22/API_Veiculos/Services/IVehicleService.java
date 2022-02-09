package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;

import java.util.List;
import java.util.Set;

public interface IVehicleService {

    public Vehicle save(String emailOuCpf, Vehicle vehicle) throws IllegalArgumentException;

    public Set<Vehicle> getVehiclesByOwner(String emailOuCpf);

    public List<Vehicle> updatePrices();
}
