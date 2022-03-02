package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Owner;
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

import java.util.Set;

@RestController
@RequestMapping("/veiculo")
public class VehicleController {

    Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IOwnerService ownerService;

    @RequestMapping(value = "/{email_ou_cpf}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions(){
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.GET ,HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping("/{vehicle_id}")
    public ResponseEntity<?> getVehicleById(@PathVariable(value="vehicle_id") String vehicleId){
        try {
            logger.info("Solicitado o veículo com id: " + vehicleId);

            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(vehicle);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao retornar o veículo de id: " + vehicleId);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{email_ou_cpf}")
    public ResponseEntity<?> addVehicleToOwner(@PathVariable(value="email_ou_cpf") String emailOrCpf, @RequestBody Vehicle vehicle){
        try {
            logger.info("O usuário com "  + (emailOrCpf.contains("@")? "email " : "cpf ") + emailOrCpf + " solicita o cadastro do veículo " + vehicle  );

            Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);

            vehicleService.verifyVehicleInfo(vehicle);

            Vehicle addedVehicle = vehicleService.addVehicleToOwner(owner, vehicle);

            logger.info("Cadastro realizado com sucesso do veículo: " + addedVehicle);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(addedVehicle);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao cadastrar veículo: " + vehicle);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/registro/{email_ou_cpf}/{vehicle_id}")
    public ResponseEntity<?> removeVehicleFromOwner(
            @PathVariable(value="email_ou_cpf") String emailOrCpf,
            @PathVariable(value="vehicle_id") String vehicleId){
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("");

        } catch (IllegalArgumentException e){
            logger.error("Erro ao remover o veículo: " + "" + " do usuário: " +"");
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @RequestMapping(value = "/lista-completa/{email_ou_cpf}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getListOptions(){
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET ,HttpMethod.OPTIONS)
                .build();
    }

    @GetMapping("/lista-completa/{email_ou_cpf}")
    public ResponseEntity<?> getVehicles(@PathVariable(value="email_ou_cpf") String emailOuCpf){
        try {
            logger.info("Solicitando lista de veículos do usuário com "  + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Set<Vehicle> vehicles = vehicleService.getOwnerVehiclesByEmailOrCpf(emailOuCpf);

            logger.info("Retornado veículos do usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf + ": " + vehicles);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(vehicles);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao retornar lista de veículos do usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
