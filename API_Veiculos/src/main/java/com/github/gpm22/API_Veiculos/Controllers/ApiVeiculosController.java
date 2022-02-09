package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@RestController
@RequestMapping("/apiveiculos/v1")
public class ApiVeiculosController {

    Logger logger = LoggerFactory.getLogger(ApiVeiculosController.class);

    @Autowired
    private IVehicleService vehicleService;

    @Autowired
    private IOwnerService ownerService;

    @PostMapping("/usuario")
    public ResponseEntity createOwner(@RequestBody Owner owner) {
        try {
            logger.info("Iniciando cadastro do usuario: " + owner);
            Owner newOwner = ownerService.save(owner);
            logger.info("Cadastro realizado com sucesso do usuario: " + newOwner);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newOwner);
        } catch (IllegalArgumentException e) {
            logger.error("Erro durante cadastro do usuário:" + owner);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{email_ou_cpf}")
    public ResponseEntity getOwner(@PathVariable(value="email_ou_cpf") String emailOuCpf) {
        try {
            logger.info("Solicitado usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Owner owner = ownerService.getByCpfOrEmail(emailOuCpf);

            logger.info("Retornando usuário " + owner);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(owner);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao retornar usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar-veiculo/{email_ou_cpf}")
    public ResponseEntity createVehicle(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Vehicle vehicle){
        try {
            logger.info("O usuário com "  + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf + " solicita o cadastro do veículo " + vehicle  );

            Vehicle newVehicle = vehicleService.save(emailOuCpf, vehicle);

            logger.info("Cadastro realizado com sucesso do veículo: " + newVehicle);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(newVehicle);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao cadastrar veículo: " + vehicle);
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/lista-de-veiculos/{email_ou_cpf}")
    public ResponseEntity getVehicles(@PathVariable(value="email_ou_cpf") String emailOuCpf){
        try {
            logger.info("Solicitando lista de veículos do usuário com "  + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Set<Vehicle> vehicles = vehicleService.getVehiclesByOwner(emailOuCpf);

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

