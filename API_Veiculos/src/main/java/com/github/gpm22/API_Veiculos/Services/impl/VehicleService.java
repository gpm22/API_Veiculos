package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Client.ApiFipeClient;
import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import com.github.gpm22.API_Veiculos.Utils.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ApiFipeClient apiFipeClient;

    @Override
    public Vehicle save(String emailOuCpf, Vehicle vehicle) throws IllegalArgumentException {

        Owner owner = ownerRepository.findByCpfOrEmail(emailOuCpf, emailOuCpf);

        if (owner == null) {
            throw new IllegalArgumentException("Não existe usuário com o email ou cpf: " + emailOuCpf);
        }

        if (vehicle.getBrand().equals("") || vehicle.getBrand() == null) {
            throw new IllegalArgumentException("Parâmetro marca não pode ser vazio!");
        }

        if (vehicle.getModel().equals("") || vehicle.getModel() == null) {
            throw new IllegalArgumentException("Parâmetro modelo não pode ser vazio!");
        }

        if (vehicle.getYear().equals("") || vehicle.getYear() == null) {
            throw new IllegalArgumentException("Parâmetro ano não pode ser vazio!");
        }

        if (vehicle.getType().equals("") || vehicle.getType() == null) {
            throw new IllegalArgumentException("Parâmetro tipo não pode ser vazio!\n Deve ser: carros, motos ou caminhoes.");
        }

        vehicle.setRotationDay(Commons.rotationDay(vehicle.getYear()));
        vehicle.setRotationActive(Commons.isRotationActive(vehicle.getRotationDay()));
        vehicle.setPrice(this.getFipePrice(vehicle));
        owner.addVehicle(vehicle);

        return vehicleRepository.save(vehicle);
    }

    private String getFipePrice(Vehicle vehicle) {

        String type = vehicle.getType();
        String codeBrand = getCodeBrand(type, vehicle.getBrand());
        String codeModel = getCodeModel(type, codeBrand, vehicle.getModel());
        String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear());

        return apiFipeClient.getFipePrice(type, codeBrand, codeModel, fipeYear).getValor();
    }

    private String getCodeBrand(String type, String vehicleBrand) {
        return apiFipeClient.getBrandList(type).filter(brand -> brand.getNome().equals(vehicleBrand)).findAny().get().getCodigo();
    }

    private String getCodeModel(String type, String codeBrand, String vehicleModel) {
        return apiFipeClient.getModelList(type, codeBrand).sequential().filter(model -> model.getNome().equals(vehicleModel)).findAny().get().getCodigo();
    }

    private String getFipeYear(String type, String codeBrand, String codeModel, String vehicleYear) {
        return apiFipeClient.getYearlList(type, codeBrand, codeModel).filter(year -> year.getNome().equals(vehicleYear)).findAny().get().getCodigo();
    }

    @Override
    public Set<Vehicle> getVehiclesByOwner(String emailOuCpf) {
        Optional<Owner> optional = Optional.ofNullable(ownerRepository.findByCpfOrEmail(emailOuCpf, emailOuCpf));

        if (optional.isPresent()) {
            Set<Vehicle> vehicles = optional.get().getVehicles();
            vehicles.forEach(n -> n.setRotationActive(Commons.isRotationActive(n.getRotationDay())));
            return vehicles;
        } else {
            throw new IllegalArgumentException("Não existe usuário com o " + (emailOuCpf.contains("@") ? "email" : "cpf") + ": " + emailOuCpf);
        }
    }
}