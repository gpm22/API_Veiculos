package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import com.github.gpm22.API_Veiculos.Utils.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class OwnerService implements IOwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Owner saveOrUpdateOwner(Owner owner) throws IllegalArgumentException {
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
        String namePattern = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        if(!Pattern.compile(namePattern).matcher(ownerName).matches()){
            throw new IllegalArgumentException("Nome: " + ownerName + " é inválido!");
        }
    }

    private void validateOwnerCpf(String ownerCpf){
        if(!Commons.cpfValidation(ownerCpf)){
            throw new IllegalArgumentException("CPF: " + ownerCpf + " é inválido!");
        }
    }

    private void validateOwnerEmail(String ownerEmail) throws IllegalArgumentException {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(!Pattern.compile(emailPattern).matcher(ownerEmail).matches()){
            throw new IllegalArgumentException("Email: " + ownerEmail + " é inválido!");
        }
    }

    private void validateOwnerBirthDate(Date ownerBirthDate){
        if (ownerBirthDate == null) {
            throw new IllegalArgumentException("Data de aniversário inválida!");
        }
    }

    private void verifyIfCpfIsUnique(Owner owner) throws IllegalArgumentException{
        if (ownerRepository.findByCpf(owner.getCpf()) != null) {
            throw new IllegalArgumentException("CPF: " + owner.getCpf() + " já utilizado!");
        }
    }

    private void verifyIfEmailIsUnique(Owner owner) throws IllegalArgumentException{
        if (ownerRepository.findByEmail(owner.getEmail()) != null) {
            throw new IllegalArgumentException("Email: " + owner.getEmail() + " já utilizado!");
        }
    }

    @Override
    public Owner getOwnerByCpfOrEmail(String cpfOrEmail) {
        Optional<Owner> owner = Optional.ofNullable(ownerRepository.findByCpfOrEmail(cpfOrEmail, cpfOrEmail));

        if (owner.isPresent()) {
            updateVehiclesRotationActive(owner.get().getVehicles());
            return owner.get();
        } else {
            throw new IllegalArgumentException("Não existe usuário com o " + (cpfOrEmail.contains("@") ? "email" : "cpf") + ": " + cpfOrEmail);
        }
    }

    private void updateVehiclesRotationActive(Collection<Vehicle> vehicles){
        vehicles.forEach(n -> n.setRotationActive(Commons.isRotationActive(n.getRotationDay())));
    }

    @Override
    public void validateUpdatedOwnerInformation(Owner owner, Owner updatedOwner){
        validateOwnerInformation(updatedOwner);
        compareOwnerAndUpdatedOwner(owner,updatedOwner);
    }

    private void compareOwnerAndUpdatedOwner(Owner owner, Owner updatedOwner) {
        if(!owner.getCpf().equals(updatedOwner.getCpf())){
            verifyIfCpfIsUnique(updatedOwner);
        }
        if(!owner.getEmail().equals(updatedOwner.getEmail())){
            verifyIfEmailIsUnique(updatedOwner);
        }
    }

    @Override
    public void updateOwnerInfo(Owner owner, Owner updatedOwner) {
        owner.setCpf(updatedOwner.getCpf());
        owner.setBirthDate(updatedOwner.getBirthDate());
        owner.setEmail(updatedOwner.getEmail());
        owner.setName(updatedOwner.getName());
    }

    @Override
    public Owner deleteOwnerByCpfOrEmail(String emailOuCpf) throws IllegalArgumentException {
            Owner owner = this.getOwnerByCpfOrEmail(emailOuCpf);
            ownerRepository.delete(owner);
            return owner;
    }

}
