package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
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

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions(){
        return ResponseEntity
                .ok()
                .allow(HttpMethod.POST ,HttpMethod.OPTIONS)
                .build();
    }

    @PostMapping
    public ResponseEntity<?> createOwner(@RequestBody Owner owner) {
        try {
            logger.info("Iniciando cadastro do usuario: " + owner);
            ownerService.validateNewOwnerInformation(owner);
            Owner newOwner = ownerService.saveOrUpdateOwner(owner);
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

    @RequestMapping(value="/{email_ou_cpf}", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOwnerOptions(){
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.DELETE, HttpMethod.PUT, HttpMethod.OPTIONS)
                .build();
    }

    @PutMapping("/{email_ou_cpf}")
    public ResponseEntity<?> updateOwner(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Owner updatedOwner) {
        try {
            logger.info("Solicitado alteração do usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Owner owner = ownerService.getOwnerByCpfOrEmail(emailOuCpf);

            ownerService.validateUpdatedOwnerInformation(owner, updatedOwner);
            ownerService.updateOwnerInfo(owner, updatedOwner);

            Owner savedOwner = ownerService.saveOrUpdateOwner(owner);

            logger.info("Retornando usuário " + savedOwner);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(savedOwner);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao atualizar usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{email_ou_cpf}")
    public ResponseEntity<?> deleteOwner(@PathVariable(value="email_ou_cpf") String emailOuCpf) {
        try {
            logger.info("Solicitado exclusão usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Owner owner = ownerService.deleteOwnerByCpfOrEmail(emailOuCpf);

            logger.info("Exclusão concluída do usuário " + owner);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(owner);

        } catch (IllegalArgumentException e){
            logger.error("Erro ao excluir usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{email_ou_cpf}")
    public ResponseEntity<?> getOwner(@PathVariable(value="email_ou_cpf") String emailOuCpf) {
        try {
            logger.info("Solicitado usuário com " + (emailOuCpf.contains("@")? "email " : "cpf ") + emailOuCpf );

            Owner owner = ownerService.getOwnerByCpfOrEmail(emailOuCpf);

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

}
