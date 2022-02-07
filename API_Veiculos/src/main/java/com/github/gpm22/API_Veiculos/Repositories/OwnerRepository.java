package com.github.gpm22.API_Veiculos.Repositories;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Owner findByCpf(final String cpf);
    
    Owner findByEmail(final String email);

}
