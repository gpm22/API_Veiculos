package com.github.gpm22.API_Veiculos.Controllers;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiveiculos/v1/usuario")
public class OwnerController {

    Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    private IOwnerService ownerService;

    @PostMapping
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

    @GetMapping("/{email_ou_cpf}")
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
}
