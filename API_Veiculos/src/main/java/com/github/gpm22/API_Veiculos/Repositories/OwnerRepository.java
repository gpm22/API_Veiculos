package com.github.gpm22.API_Veiculos.Repositories;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByCpf(final String cpf);

    Owner findByEmail(final String email);

    Owner findByCpfOrEmail(final String cpf, final String email);

}
