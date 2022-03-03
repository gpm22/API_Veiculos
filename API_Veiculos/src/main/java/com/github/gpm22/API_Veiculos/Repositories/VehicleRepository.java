package com.github.gpm22.API_Veiculos.Repositories;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findByModelAndYear(String model, String year);
    Set<Vehicle> findByOwnersIsNull();
}
