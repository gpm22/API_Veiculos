package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Owner;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class OwnerController {

    Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    private IOwnerService ownerService;

    @Autowired
    private IVehicleService vehicleService;

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        try {
            return createOwnerResponse(owner);
        } catch (IllegalArgumentException e) {
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Erro durante cadastro do usuário:" + owner, logger);
        }
    }

    private ResponseEntity<Owner> createOwnerResponse(Owner owner){
        logger.info("Iniciando cadastro do usuario: {}", owner);
        ownerService.validateNewOwnerInformation(owner);
        Owner newOwner = ownerService.saveOrUpdateOwner(owner);
        logger.info("Cadastro realizado com sucesso do usuario: {}", newOwner);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newOwner);
    }

    @RequestMapping(value = "/{email_ou_cpf}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOwnerOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.OPTIONS)
                .build();
    }

    @PutMapping("/{email_ou_cpf}")
    public ResponseEntity<?> updateOwner(@PathVariable(value = "email_ou_cpf") String emailOrCpf, @RequestBody Owner updatedOwner) {
        try {
            return updateOwnerResponse(emailOrCpf, updatedOwner);
        } catch (IllegalArgumentException e) {
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.NOT_FOUND, "Erro ao atualizar " + formatUserMessage(emailOrCpf), logger);
        }
    }

    private ResponseEntity<Owner> updateOwnerResponse(String emailOrCpf, Owner updatedOwner){
        logger.info("Solicitado alteração do {}", formatUserMessage(emailOrCpf));

        Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);

        ownerService.validateUpdatedOwnerInformation(owner, updatedOwner);
        owner.updateInfoWith(updatedOwner);

        Owner savedOwner = ownerService.saveOrUpdateOwner(owner);

        logger.info("Retornando usuário atualizado {}", savedOwner);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(savedOwner);
    }

    @DeleteMapping("/{email_ou_cpf}")
    public ResponseEntity<?> deleteOwner(@PathVariable(value = "email_ou_cpf") String emailOrCpf) {
        try {
            return deleteOwnerResponse(emailOrCpf);
        } catch (IllegalArgumentException e) {
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.NOT_FOUND, "Erro ao excluir o " + formatUserMessage(emailOrCpf), logger);
        }
    }

    private ResponseEntity<Owner> deleteOwnerResponse(String emailOrCpf){
        logger.info("Solicitado exclusão do {}", formatUserMessage(emailOrCpf));

        Owner owner = ownerService.deleteOwnerByCpfOrEmail(emailOrCpf);

        logger.info("Exclusão concluída do usuário {}", owner);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(owner);
    }

    @GetMapping("/{email_ou_cpf}")
    public ResponseEntity<?> getOwner(@PathVariable(value = "email_ou_cpf") String emailOrCpf) {
        try {
            return getOwnerResponse(emailOrCpf);
        } catch (IllegalArgumentException e) {
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.NOT_FOUND, "Erro ao retornar " + formatUserMessage(emailOrCpf), logger);
        }
    }

    private ResponseEntity<Owner> getOwnerResponse(String emailOrCpf){
        logger.info("Solicitado info sobre o {}", formatUserMessage(emailOrCpf));

        Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);

        logger.info("Retornando usuário solictiado {}", owner);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(owner);
    }

    @RequestMapping(value = "/{email_ou_cpf}/registro-veiculo/{vehicle_id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getVehicleRegisterOptions() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.OPTIONS)
                .build();
    }

    @PutMapping("/{email_ou_cpf}/registro-veiculo/{vehicle_id}")
    public ResponseEntity<?> registerVehicle(@PathVariable(value = "email_ou_cpf") String emailOrCpf, @PathVariable(value = "vehicle_id") long vehicleId ) {
        try {
            return  registerVehicleResponse(emailOrCpf, vehicleId);
        } catch (IllegalArgumentException e) {
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Erro ao cadastrar veículo com id: " + vehicleId, logger);
        }
    }

    private ResponseEntity<Vehicle> registerVehicleResponse(String emailOrCpf, long vehicleId){
        logger.info("O {} solicita o cadastro do veículo com id{}", formatUserMessage(emailOrCpf), vehicleId);

        Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);

        Vehicle addedVehicle = vehicleService.getVehicleById(vehicleId);

        owner.addVehicle(addedVehicle);
        ownerService.saveOrUpdateOwner(owner);

        addedVehicle.addOwner(owner);
        vehicleService.saveOrUpdateVehicle(addedVehicle);

        logger.info("Cadastro realizado com sucesso do veículo: {}", addedVehicle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addedVehicle);
    }

    @DeleteMapping("/{email_ou_cpf}/registro-veiculo/{vehicle_id}")
    public ResponseEntity<?> removeVehicleRegister(@PathVariable(value = "email_ou_cpf") String emailOrCpf, @PathVariable(value = "vehicle_id") long vehicleId) {
        try {
            return removeVehicleResponse(emailOrCpf, vehicleId);
        } catch (IllegalArgumentException e) {
            String errorMessage = "Erro ao remover o veículo com id: "
                    + vehicleId + " do "
                    + formatUserMessage(emailOrCpf);
            return ResponseEntityBuilder.buildErrorResponse(e, HttpStatus.BAD_REQUEST , errorMessage , logger);
        }
    }

    private ResponseEntity<Vehicle> removeVehicleResponse(String emailOrCpf, long vehicleId){
        logger.info("O {} solicita a remoção do veículo com id {}", formatUserMessage(emailOrCpf), vehicleId);

        Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);

        Vehicle removedVehicle = vehicleService.getVehicleById(vehicleId);

        owner.removeVehicle(removedVehicle);
        ownerService.saveOrUpdateOwner(owner);

        removedVehicle.removeOwner(owner);
        vehicleService.saveOrUpdateVehicle(removedVehicle);

        logger.info("Remoção realizada com sucesso do veículo: {}", removedVehicle);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(removedVehicle);
    }

    private String formatUserMessage(String emailOrCpf){
        return "usuário com " + (emailOrCpf.contains("@") ? "email " : "cpf ") + emailOrCpf;
    }

}
