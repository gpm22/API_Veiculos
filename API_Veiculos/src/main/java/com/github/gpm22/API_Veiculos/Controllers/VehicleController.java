package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/veiculo")
public class VehicleController {

    Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IOwnerService ownerService;

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping
    public ResponseEntity<?> getVehicles() {
        try {
            logger.info("Solicitados todos os veículos.");

            Collection<Vehicle> vehicles = vehicleService.getAllVehicles();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(vehicles);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao retornar todos os veículos");
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{vehicle_id}")
    public ResponseEntity<?> getVehicleById(@PathVariable(value = "vehicle_id") long vehicleId) {
        try {
            logger.info("Solicitado o veículo com id: " + vehicleId);

            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(vehicle);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao retornar o veículo de id: " + vehicleId);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            logger.info("Solicitado o cadastro do veículo " + vehicle);

            vehicleService.verifyVehicleInfo(vehicle);
            vehicleService.verifyIfVehicleAlreadyExists(vehicle);
            vehicleService.setVehicleInformations(vehicle);

            Vehicle addedVehicle = vehicleService.saveOrUpdateVehicle(vehicle);

            logger.info("Cadastro realizado com sucesso do veículo: " + addedVehicle);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(addedVehicle);

        } catch (IllegalArgumentException e) {
            logger.error("Erro ao cadastrar veículo: " + vehicle);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
