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

    @RequestMapping(value = "/cadastrar/{email_ou_cpf}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getRegisterOptions(){
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST ,HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping("/cadastrar/{email_ou_cpf}")
    public ResponseEntity<?> createVehicle(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Vehicle vehicle){
        try {
            logger.info("O usuário com "  + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf + " solicita o cadastro do veículo " + vehicle  );

            Owner owner = ownerService.getOwnerByCpfOrEmail(emailOuCpf);

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
