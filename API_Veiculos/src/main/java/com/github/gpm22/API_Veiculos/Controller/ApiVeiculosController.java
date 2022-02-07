package com.github.gpm22.API_Veiculos.Controller;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Services.ApiVeiculosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/apiveiculos/v1")
public class ApiVeiculosController {
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ApiVeiculosService service;

    @PostMapping("/usuario")
    public ResponseEntity createOwner(@RequestBody Owner owner) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(service.save(owner));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/cadastrar-veiculo/{email_ou_cpf}")
    public ResponseEntity createVehicle(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Vehicle vehicle){
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(service.save(emailOuCpf, vehicle));

        } catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/lista-de-veiculos/{email_ou_cpf}")
    public ResponseEntity getVehicles(@PathVariable(value="email_ou_cpf") String emailOuCpf){
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(service.getVehiclesByOwner(emailOuCpf));

        } catch (IllegalArgumentException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}

