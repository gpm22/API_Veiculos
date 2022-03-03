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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;


@Service
public class VehicleService implements IVehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ApiFipeClient apiFipeClient;

    private final String[] vehicleTypes = {"carros", "motos", "caminhoes"};

    @Override
    public Vehicle saveOrUpdateVehicle(Vehicle vehicle) throws IllegalArgumentException {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public int deleteVehiclesWithoutOwners(Vehicle vehicle) {
        Set<Vehicle> vehiclesWithoutOwners = vehicleRepository.findByOwnersIsNull();
        vehicleRepository.deleteAll(vehiclesWithoutOwners);
        return vehiclesWithoutOwners.size();
    }

    @Override
    public void verifyIfVehicleAlreadyExists(Vehicle vehicle) {
        Vehicle existingVehicle = vehicleRepository.findByModelAndYear(vehicle.getModel(), vehicle.getYear());

        if (existingVehicle != null) {
            throw new IllegalArgumentException("Esse veículo já foi anteriormente cadastrado: " + existingVehicle.getId());
        }
    }

    @Override
    public void setVehicleInformations(Vehicle vehicle){
        vehicle.setRotationDay(Commons.rotationDay(vehicle.getYear()));
        vehicle.setRotationActive(Commons.isRotationActive(vehicle.getRotationDay()));
        vehicle.setPrice(getFipePrice(vehicle));
    }

    private String getFipePrice(Vehicle vehicle) {

        String type = vehicle.getType();
        String codeBrand = getCodeBrand(type, vehicle.getBrand());
        String codeModel = getCodeModel(type, codeBrand, vehicle.getModel());
        String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear());

        try {
            return apiFipeClient.getFipePrice(type, codeBrand, codeModel, fipeYear).getValor();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("o parâmetro ano não condiz com o que está na API FIPE!");
        }
    }

    private String getCodeBrand(String type, String vehicleBrand) {
        try {
            return apiFipeClient.getBrandList(type).filter(brand -> brand.getNome().equals(vehicleBrand)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("O parâmetro marca não condiz com o que está na API FIPE!");
        }
    }

    private String getCodeModel(String type, String codeBrand, String vehicleModel) {
        try {
            return apiFipeClient.getModelList(type, codeBrand).sequential().filter(model -> model.getNome().equals(vehicleModel)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("o parâmetro modelo não condiz com o que está na API FIPE!");
        }
    }

    private String getFipeYear(String type, String codeBrand, String codeModel, String vehicleYear) {
        try {
            return apiFipeClient.getYearlList(type, codeBrand, codeModel).filter(year -> year.getNome().equals(vehicleYear)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("o parâmetro ano não condiz com o que está na API FIPE!");
        }
    }

    @Override
    public void verifyVehicleInfo(Vehicle vehicle) throws IllegalArgumentException {
        verifyVehicleBrand(vehicle.getBrand());
        verifyVehicleModel(vehicle.getModel());
        verifyVehicleYear(vehicle.getYear());
        verifyVehicleType(vehicle.getType());
    }

    @Override
    public Collection<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "\nDeve ser: " + vehicleTypes);
        }
    }

    private void verifyIfStringIsEmpty(String attribute, String value) {
        if (value == null) {
            throw new IllegalArgumentException("Parâmetro " + attribute + " não pode ser vazio!");
        }

        if (value.isEmpty()) {
            throw new IllegalArgumentException("Parâmetro " + attribute + " não pode ser vazio!");
        }
    }

    @Override
    public int updateVehiclesPrices() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        vehicles.forEach(vehicle -> vehicle.setPrice(this.getFipePrice(vehicle)));
        return vehicleRepository.saveAll(vehicles).size();
    }

    @Override
    public Vehicle getVehicleById(long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle.isPresent()) {
            return vehicle.get();
        } else {
            throw new IllegalArgumentException("Não existe veículo com id: " + vehicleId);
        }
    }

    @Override
    public void removeOwnerFromVehicle(Vehicle removedVehicle, Owner owner) {
        verifyIfVehicleHasOwner(removedVehicle, owner);
        removedVehicle.getOwners().remove(owner);
        saveOrUpdateVehicle(removedVehicle);
    }

    private void verifyIfVehicleHasOwner(Vehicle vehicle, Owner owner) {
        if(!vehicle.getOwners().contains(owner)){
            throw new IllegalArgumentException("O veículo de id " + vehicle.getId() + " não possui o usuário de cpf: " + owner.getCpf());
        }
    }

    @Override
    public void addOwnerToVehicle(Vehicle addedVehicle, Owner owner) {
        addedVehicle.getOwners().add(owner);
        saveOrUpdateVehicle(addedVehicle);
    }
}
