package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Clients.ApiFipeClient;
import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import com.github.gpm22.API_Veiculos.Utils.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private ApiFipeClient apiFipeClient;

    private String[] vehicleTypes = {"carros", "motos", "caminhoes"};

    @Override
    public Vehicle addVehicleToOwner(Owner owner, Vehicle vehicle) throws IllegalArgumentException {

        Vehicle existingVehicle = vehicleRepository.findByModelAndYear(vehicle.getModel(), vehicle.getYear());

        if(existingVehicle == null){
            return addNewVehicleToOwner(owner, vehicle);
        }

        if(owner.getVehicles().contains(existingVehicle)){
            throw new IllegalArgumentException("O usuário com cpf " + owner.getCpf() + " já possui esse veículo cadastrado " + existingVehicle);
        }

        return addExistingVehicleToOwner(owner, existingVehicle);
    }

    private Vehicle addNewVehicleToOwner(Owner owner ,Vehicle vehicle){
        vehicle.setRotationDay(Commons.rotationDay(vehicle.getYear()));
        vehicle.setRotationActive(Commons.isRotationActive(vehicle.getRotationDay()));
        vehicle.setPrice(getFipePrice(vehicle));
        owner.addVehicle(vehicle);

        return vehicleRepository.save(vehicle);
    }

    private Vehicle addExistingVehicleToOwner(Owner owner, Vehicle vehicle){
        vehicle.setRotationActive(Commons.isRotationActive(vehicle.getRotationDay()));
        owner.addVehicle(vehicle);
        ownerService.saveOrUpdateOwner(owner);
        return vehicle;
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
    public void verifyVehicleInfo(Vehicle vehicle) throws IllegalArgumentException {
        verifyVehicleBrand(vehicle.getBrand());
        verifyVehicleModel(vehicle.getModel());
        verifyVehicleYear(vehicle.getYear());
        verifyVehicleType(vehicle.getType());
    }

    private void verifyVehicleBrand(String brand) {
        verifyIfStringIsEmpty("marca", brand);
    }

    private void verifyVehicleModel(String model) {
        verifyIfStringIsEmpty("modelo", model);
    }

    private void verifyVehicleYear(String year) {
        verifyIfStringIsEmpty("ano", year);
    }

    private void verifyVehicleType(String type) {
        try {
            verifyIfStringIsEmpty("tipo", type);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException( e.getMessage() + "\nDeve ser: " + vehicleTypes);
        }
    }

    private void verifyIfStringIsEmpty(String attribute, String value){
        if(value == null){
            throw new IllegalArgumentException("Parâmetro " + attribute + " não pode ser vazio!");
        }

        if(value.isEmpty()) {
            throw new IllegalArgumentException("Parâmetro " + attribute + " não pode ser vazio!");
        }
    }

    @Override
    public Set<Vehicle> getOwnerVehiclesByEmailOrCpf(String emailOrCpf) throws IllegalArgumentException{
        Owner owner = ownerService.getOwnerByCpfOrEmail(emailOrCpf);
        Set<Vehicle> vehicles = owner.getVehicles();
        Commons.updateVehiclesRotationActive(vehicles);
        return vehicles;
    }

    @Override
    public List<Vehicle> updateVehiclesPrices(){
        List<Vehicle> vehicles = vehicleRepository.findAll();
        vehicles.forEach(vehicle -> vehicle.setPrice(this.getFipePrice(vehicle)));
        return vehicleRepository.saveAll(vehicles);
    }
}
