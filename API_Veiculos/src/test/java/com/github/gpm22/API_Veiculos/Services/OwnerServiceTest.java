package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Services.impl.OwnerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    @InjectMocks
    private IOwnerService ownerService = new OwnerService();

    @Mock
    private OwnerRepository ownerRepository;

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

    private void init(){
        MockitoAnnotations.openMocks(this);
        Mockito.when(ownerRepository.findByCpf(any(String.class))).thenReturn(null);
        Mockito.when(ownerRepository.findByEmail(any(String.class))).thenReturn(null);
    }

    @Test
    public void validateUpdatedOwnerInformationTestCorrect() throws ParseException {
        init();
        Owner owner = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        Owner newOwnerEqual = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        ownerService.validateUpdatedOwnerInformation(owner, newOwnerEqual);

        Owner newOwnerNewName = new Owner("Lou", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewName);

        Owner newOwnerNewEmail = new Owner("Oswald", "oswald_dudu@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewEmail);

        Owner newOwnerNewCPF = new Owner("Oswald", "oswald@gmai.com", "51545241015", formatter.parse("7-Jun-1989"));
        ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewCPF);

        Owner newOwnerNewBirthday = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1998"));
        ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewBirthday);
    }

    @Test
    public void validateUpdatedOwnerInformationTestWrongThrows() throws ParseException {

        Owner owner = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));

        Owner newOwnerNewWrongName = new Owner("", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewWrongName));

        Owner newOwnerNewWrongEmail = new Owner("Oswald", "oswald_dudugmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewWrongEmail));

        Owner newOwnerNewWrongCPF = new Owner("Oswald", "oswald@gmai.com", "55545241015", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewWrongCPF));

        Owner newOwnerNewWrongBirthday = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateUpdatedOwnerInformation(owner, newOwnerNewWrongBirthday));
    }

    @Test
    public void validateNewOwnerInformationTestCorrect() throws ParseException {
        init();
        Owner owner = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        ownerService.validateNewOwnerInformation(owner);
    }

    @Test
    public void validateNewOwnerInformationTestWrongThrows() throws ParseException {
        Owner wrongName = new Owner("", "oswald@gmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateNewOwnerInformation(wrongName));

        Owner wrongEmail = new Owner("Oswald", "oswald_dudugmai.com", "126.764.550-44", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateNewOwnerInformation(wrongEmail));

        Owner wrongCPF = new Owner("Oswald", "oswald@gmai.com", "55545241015", formatter.parse("7-Jun-1989"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateNewOwnerInformation(wrongCPF));

        Owner wrongBirthday = new Owner("Oswald", "oswald@gmai.com", "126.764.550-44", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ownerService.validateNewOwnerInformation(wrongBirthday));
    }
}
