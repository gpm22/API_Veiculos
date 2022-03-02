package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;

public interface IOwnerService {

    Owner saveOrUpdateOwner(Owner owner) throws IllegalArgumentException;

    Owner getOwnerByCpfOrEmail(String cpfOrEmail);

    void validateUpdatedOwnerInformation(Owner owner, Owner updatedOwner);

    void updateOwnerInfo(Owner owner, Owner updatedOwner);

    Owner deleteOwnerByCpfOrEmail(String emailOuCpf);

    void validateNewOwnerInformation(Owner owner) throws IllegalArgumentException;

    Vehicle addVehicleToOwner(Owner owner, Vehicle vehicle);

    Vehicle removeVehicleFromOwner(Owner owner, Vehicle vehicle);
}
