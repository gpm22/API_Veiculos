package com.github.gpm22.API_Veiculos.Services.impl;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Services.IOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class OwnerService implements IOwnerService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Owner save(Owner owner) throws IllegalArgumentException {
        if (!this.ownerNameValidation(owner.getName())) {
            throw new IllegalArgumentException("Nome: " + owner.getName() + " é inválido!");
        }

        if (!this.ownerCpfValidation(owner.getCpf())) {
            throw new IllegalArgumentException("CPF: " + owner.getCpf() + " é inválido!");
        }

        if (!this.ownerEmailValidation(owner.getEmail())) {
            throw new IllegalArgumentException("Email: " + owner.getEmail() + " é inválido!");
        }

        if (owner.getBirthDate() == null) {
            throw new IllegalArgumentException("Data de aniversário inválida!");
        }

        if (ownerRepository.findByCpf(owner.getCpf()) != null) {
            throw new IllegalArgumentException("CPF: " + owner.getCpf() + " já utilizado!");
        }

        if (ownerRepository.findByEmail(owner.getEmail()) != null) {
            throw new IllegalArgumentException("Email: " + owner.getEmail() + " já utilizado!");
        }

        return ownerRepository.save(owner);
    }

    private Boolean ownerNameValidation(String ownerName) {
        String nameValidation = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        return Pattern.compile(nameValidation).matcher(ownerName).matches();
    }

    private Boolean ownerEmailValidation(String ownerEmail) {
        String emailValidation = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailValidation).matcher(ownerEmail).matches();
    }

    private Boolean ownerCpfValidation(String ownerCpf) {
        String cpfValidation = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern.compile(cpfValidation).matcher(ownerCpf).matches();
    }
}
