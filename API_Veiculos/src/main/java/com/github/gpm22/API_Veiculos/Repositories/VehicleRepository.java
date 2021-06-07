package com.github.gpm22.API_Veiculos.Repositories;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
