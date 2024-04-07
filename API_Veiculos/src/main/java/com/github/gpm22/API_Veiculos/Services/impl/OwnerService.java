package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import com.github.gpm22.API_Veiculos.Utils.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Pattern;

@Service
public class OwnerService implements IOwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Owner saveOrUpdateOwner(Owner owner) throws IllegalArgumentException {
        owner.setCpf(CPFValidator.formatCPF(owner.getCpf()));
        return ownerRepository.save(owner);
    }

    @Override
    public void validateNewOwnerInformation(Owner owner) throws IllegalArgumentException{
        validateOwnerInformation(owner);
        verifyIfCpfIsUnique(owner);
        verifyIfEmailIsUnique(owner);
    }

    private void validateOwnerInformation(Owner owner) throws IllegalArgumentException{
        validateOwnerName(owner.getName());
        validateOwnerCpf(owner.getCpf());
        validateOwnerEmail(owner.getEmail());
        validateOwnerBirthDate(owner.getBirthDate());
    }

    private void validateOwnerName(String ownerName) throws IllegalArgumentException{
        if(ownerName == null || ownerName.isBlank())
            throw new IllegalArgumentException("Nome: " + ownerName + " é inválido! Não pode ser apenas espaços em branco.");
    }

    private void validateOwnerCpf(String ownerCpf){
        if(!CPFValidator.validateCPF(ownerCpf))
            throw new IllegalArgumentException("CPF: " + ownerCpf + " é inválido!");
    }

    private void validateOwnerEmail(String ownerEmail) throws IllegalArgumentException {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(!Pattern.compile(emailPattern).matcher(ownerEmail).matches())
            throw new IllegalArgumentException("Email: " + ownerEmail + " é inválido!");
    }

    private void validateOwnerBirthDate(Date ownerBirthDate){
        if (ownerBirthDate == null)
            throw new IllegalArgumentException("Data de aniversário inválida!");
    }

    private void verifyIfCpfIsUnique(Owner owner) throws IllegalArgumentException{
        String formattedCPF = CPFValidator.formatCPF(owner.getCpf());
        if (ownerRepository.findByCpf(formattedCPF) != null)
            throw new IllegalArgumentException("CPF: " + formattedCPF + " já utilizado!");
    }

    private void verifyIfEmailIsUnique(Owner owner) throws IllegalArgumentException{
        if (ownerRepository.findByEmail(owner.getEmail()) != null)
            throw new IllegalArgumentException("Email: " + owner.getEmail() + " já utilizado!");
    }

    @Override
    public Owner getOwnerByCpfOrEmail(String cpfOrEmail) {
        Owner owner = ownerRepository.findByCpfOrEmail(cpfOrEmail, cpfOrEmail);

        if (owner == null)
            throw new IllegalArgumentException("Não existe usuário com o " + (cpfOrEmail.contains("@") ? "email" : "cpf") + ": " + cpfOrEmail);

        Vehicle.updateVehiclesRotationActive(owner.getVehicles());
        return owner;
    }

    @Override
    public void validateUpdatedOwnerInformation(Owner owner, Owner updatedOwner){
        validateOwnerInformation(updatedOwner);
        validateCpfChangedUniqueness(owner, updatedOwner);
        validateEmailChangedUniqueness(owner,updatedOwner);
    }

    private void validateCpfChangedUniqueness(Owner owner, Owner updatedOwner) {
        if(!owner.getCpf().equals(updatedOwner.getCpf()))
            verifyIfCpfIsUnique(updatedOwner);
    }

    private void validateEmailChangedUniqueness(Owner owner, Owner updatedOwner) {
        if(!owner.getEmail().equals(updatedOwner.getEmail()))
            verifyIfEmailIsUnique(updatedOwner);
    }

    @Override
    public Owner deleteOwnerByCpfOrEmail(String emailOuCpf) throws IllegalArgumentException {
            Owner owner = this.getOwnerByCpfOrEmail(emailOuCpf);
            ownerRepository.delete(owner);
            return owner;
    }

}
