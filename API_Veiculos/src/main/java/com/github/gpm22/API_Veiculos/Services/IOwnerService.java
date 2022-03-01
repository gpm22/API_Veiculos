package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;

public interface IOwnerService {

    public Owner validateAndSaveNewOwner(Owner owner) throws IllegalArgumentException;

    public Owner getOwnerByCpfOrEmail(String cpfOrEmail);

    public Owner updateOwner(Owner owner) throws IllegalArgumentException;

    Owner updateOwnerByCpfOrEmail(String emailOuCpf, Owner owner);

    Owner deleteOwnerByCpfOrEmail(String emailOuCpf);
}
