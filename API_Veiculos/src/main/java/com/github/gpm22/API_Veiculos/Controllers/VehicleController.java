package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import com.github.gpm22.API_Veiculos.Utils.ResponseEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/veiculo")
public class VehicleController {

    Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IOwnerService ownerService;

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.GET, HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping
    public ResponseEntity<Collection<Vehicle>> getVehicles() {
        try {
            return getVehiclesResponse();
        } catch (IllegalArgumentException e) {
            throw ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST,
                    "Erro ao retornar todos os veículos", logger);
        }
    }

    private ResponseEntity<Collection<Vehicle>> getVehiclesResponse() {
        logger.info("Solicitados todos os veículos.");

        Collection<Vehicle> vehicles = vehicleService.getAllVehicles();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(vehicles);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        try {
            return createVehicleResponse(vehicle);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Erro ao cadastrar veículo: " + vehicle;
            throw ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST, errorMessage, logger);
        }
    }

    private ResponseEntity<Vehicle> createVehicleResponse(Vehicle vehicle) {
        logger.info("Solicitado o cadastro do veículo {}", vehicle);

        vehicleService.verifyVehicleInfo(vehicle);
        vehicleService.verifyIfVehicleAlreadyExists(vehicle);

        vehicle.updateRotationInfo();
        vehicle.setPrice(vehicleService.getFipePrice(vehicle));

        Vehicle addedVehicle = vehicleService.saveOrUpdateVehicle(vehicle);

        logger.info("Cadastro realizado com sucesso do veículo: {}", addedVehicle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addedVehicle);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIdOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping("/{vehicle_id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable(value = "vehicle_id") long vehicleId) {
        try {
            return getVehicleByIdResponse(vehicleId);
        } catch (IllegalArgumentException e) {
            throw ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST,
                    "Erro ao retornar o veículo de id: " + vehicleId, logger);
        }
    }

    private ResponseEntity<Vehicle> getVehicleByIdResponse(long vehicleId) {
        logger.info("Solicitado o veículo com id: {}", vehicleId);

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(vehicle);
    }

}
