package com.github.gpm22.API_Veiculos.Controller;

import com.github.gpm22.API_Veiculos.Client.ApiVeiculosClient;
import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
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

import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/apiveiculos/v1")
public class ApiVeiculosController {
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ApiVeiculosService service;

    private ApiVeiculosClient client = new ApiVeiculosClient();


    @PostMapping("/usuario")
    public ResponseEntity createOwner(@RequestBody Owner owner) {

        if(!service.ownerNameValidation(owner.getName())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Nome: " + owner.getName() +" é inválido!");
        }

        if(!service.ownerCpfValidation(owner.getCpf())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("CPF: " + owner.getCpf() +" é inválido!");
        }

        if(!service.ownerEmailValidation(owner.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email: " + owner.getEmail() +" é inválido!");
        }

        if(!service.ownerBirthDateValidation(owner.getBirthDate())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Data: " + owner.getBirthDate() +" é inválida!");
        }

        if(ownerRepository.findByCpf(owner.getCpf()) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("CPF: " + owner.getCpf() +" já utilizado!");
        }

        if(ownerRepository.findByEmail(owner.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email: " + owner.getEmail() +" já utilizado!");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ownerRepository.save(owner));
    }

    @PostMapping("/cadastrar-veiculo/{email_ou_cpf}")
    public ResponseEntity createVehicle(@PathVariable(value="email_ou_cpf") String emailOuCpf, @RequestBody Vehicle vehicle){

        Owner owner;

        Owner ownerEmail = ownerRepository.findByEmail(emailOuCpf);

        if (ownerEmail != null) {
            owner = ownerEmail;
        } else {
            owner = ownerRepository.findByCpf(emailOuCpf);
        }

        if(owner == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Não existe usuário com o email ou cpf: " + emailOuCpf);
        }

        if(vehicle.getBrand() == "" || vehicle.getBrand() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Parâmetro marca não pode ser vazio!");
        }

        if(vehicle.getModel() == "" || vehicle.getModel() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Parâmetro modelo não pode ser vazio!");
        }

        if(vehicle.getYear() == "" || vehicle.getYear() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Parâmetro ano não pode ser vazio!");
        }

        if(vehicle.getType() == "" || vehicle.getType() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Parâmetro tipo não pode ser vazio!\n Deve ser: carros, motos ou caminhoes.");
        }

        vehicle.setRotationDay(service.rotationDay(vehicle.getYear()));
        vehicle.setRotationActive(service.isRotationActive(vehicle.getRotationDay()));
        vehicle.setPrice(service.getFipePrice(vehicle));
        owner.addVehicle(vehicle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleRepository.save(vehicle));
    }

    @GetMapping("/lista-de-veiculos/{email_ou_cpf}")
    public ResponseEntity getVehicles(@PathVariable(value="email_ou_cpf") String emailOuCpf){

        Owner owner;

        Owner ownerEmail = ownerRepository.findByEmail(emailOuCpf);

        if (ownerEmail != null) {
            owner = ownerEmail;
        } else {
            owner = ownerRepository.findByCpf(emailOuCpf);
        }

        Optional<Owner> optional = Optional.ofNullable(owner);

        if (optional.isPresent()) {
            Set<Vehicle> vehicles = optional.get().getVehicles();
            vehicles.forEach(n -> n.setRotationActive(service.isRotationActive(n.getRotationDay())));
            return ResponseEntity.ok().body(optional.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Não existe usuário com o email ou cpf: " + emailOuCpf);
        }
    }

}

