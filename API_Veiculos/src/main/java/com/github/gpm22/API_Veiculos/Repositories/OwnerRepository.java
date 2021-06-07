package com.github.gpm22.API_Veiculos.Repositories;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @Query(value = "select o from Owner o where o.cpf = ?1")
    Owner findByCpf(final String cpf);

    @Query(value = "select o from Owner o where o.email = ?1")
    Owner findByEmail(final String email);

}
