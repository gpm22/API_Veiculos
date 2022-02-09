package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;

public interface IOwnerService {

    public Owner save(Owner owner) throws IllegalArgumentException;

    public Owner getByCpfOrEmail(String cpfOrEmail);

    public Owner update(Owner owner) throws IllegalArgumentException;

    Owner updateByCpfOrEmail(String emailOuCpf, Owner owner);

    Owner deleteByCpfOrEmail(String emailOuCpf);
}
