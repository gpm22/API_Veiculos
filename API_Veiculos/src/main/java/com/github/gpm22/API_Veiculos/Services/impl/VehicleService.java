package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Clients.ApiFipeClient;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import com.github.gpm22.API_Veiculos.Utils.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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

    private final String URI_BASE_API_FIPE = "https://parallelum.com.br/fipe/api/v1/";

    private final Set<String> vehicleTypes = new HashSet<>(Arrays.asList("carros", "motos", "caminhoes", "fipe"));

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

        String type = vehicle.getType().toLowerCase();
        String codeBrand = getCodeBrand(type, vehicle.getBrand().toLowerCase());
        String codeModel = getCodeModel(type, codeBrand, vehicle.getModel().toLowerCase());
        String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear().toLowerCase());

        return apiFipeClient.getFipePrice(type, codeBrand, codeModel, fipeYear).getValor();

    }

    private String getCodeBrand(String type, String vehicleBrand) {

        try {
            return apiFipeClient.getBrandList(type).filter(brand -> brand.getNome().toLowerCase().equals(vehicleBrand)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("O parâmetro marca não condiz com o que está na API FIPE!\n Consultar valores possíveis em: " + URI_BASE_API_FIPE +type+"/marcas");
        }
    }

    private String getCodeModel(String type, String codeBrand, String vehicleModel) {
        try {
            return apiFipeClient.getModelList(type, codeBrand).sequential().filter(model -> model.getNome().toLowerCase().equals(vehicleModel)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("O parâmetro modelo não condiz com o que está na API FIPE! \n Consultar valores possíveis em: " + URI_BASE_API_FIPE +type+ "/marcas/" +codeBrand+"/modelos");
        }
    }

    private String getFipeYear(String type, String codeBrand, String codeModel, String vehicleYear) {
        try {
            return apiFipeClient.getYearlList(type, codeBrand, codeModel).filter(year -> year.getNome().toLowerCase().equals(vehicleYear)).findAny().get().getCodigo();
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("O parâmetro ano não condiz com o que está na API FIPE! \n Consultar valores possíveis em: " + URI_BASE_API_FIPE +type+ "/marcas/" +codeBrand+ "/modelos/" +codeModel+"/anos");
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
        if(!vehicleTypes.contains(type)){
            throw new IllegalArgumentException("O parâmetro tipo não condiz com o que está na API FIPE!\nDeve ser: " + vehicleTypes);
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

}
