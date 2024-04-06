package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Clients.ApiFipe.ApiFipeClient;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.ApiFipeURL;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Year;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
import com.github.gpm22.API_Veiculos.Services.IVehicleService;
import com.github.gpm22.API_Veiculos.Utils.RotationDay;
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
    private ApiFipeClient apiFipeClient;

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
    public void setVehicleInformationsAboutRotationAndPrice(Vehicle vehicle){
        vehicle.setRotationDay(RotationDay.getRotationDay(vehicle.getYear()));
        vehicle.setRotationActive(RotationDay.isRotationDayActive(vehicle.getRotationDay()));
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

        Brand[] brands = apiFipeClient.getBrandList(type);

        for(Brand brand: brands)
            if(brand.getNome().toLowerCase().equals(vehicleBrand))
                return brand.getCodigo();

        throw new IllegalArgumentException("O parâmetro marca não condiz com o que está na API FIPE!\n Consultar valores possíveis em: " + ApiFipeURL.getBrandURI(type));
    }

    private String getCodeModel(String type, String codeBrand, String vehicleModel) {
        Model[] models = apiFipeClient.getModelList(type, codeBrand);

        for(Model model: models)
            if(model.getNome().toLowerCase().equals(vehicleModel))
                return model.getCodigo();

        throw new IllegalArgumentException("O parâmetro modelo não condiz com o que está na API FIPE! \n Consultar valores possíveis em: " + ApiFipeURL.getModelURI(type, codeBrand));
    }

    private String getFipeYear(String type, String codeBrand, String codeModel, String vehicleYear) {

        Year[] years = apiFipeClient.getYearList(type, codeBrand, codeModel);

        for(Year year: years)
            if(year.getNome().toLowerCase().equals(vehicleYear))
                return year.getCodigo();

        throw new IllegalArgumentException("O parâmetro ano não condiz com o que está na API FIPE! \n Consultar valores possíveis em: " + ApiFipeURL.getYearURI(type, codeBrand, codeModel));
    }

    @Override
    public void verifyVehicleInfo(Vehicle vehicle) throws IllegalArgumentException {
        verifyIfStringIsEmpty("marca", vehicle.getBrand());
        verifyIfStringIsEmpty("modelo", vehicle.getModel());
        verifyIfStringIsEmpty("ano", vehicle.getYear());
        verifyVehicleType(vehicle.getType());
    }

    @Override
    public Collection<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    private void verifyVehicleType(String type) {
        if(!ApiFipeURL.VEHICLE_TYPES.contains(type))
            throw new IllegalArgumentException("O parâmetro tipo não condiz com o que está na API FIPE!\nDeve ser um dos seguintes valores: " + ApiFipeURL.VEHICLE_TYPES);
    }

    private void verifyIfStringIsEmpty(String attribute, String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("Parâmetro " + attribute + " não pode ser vazio!");
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
