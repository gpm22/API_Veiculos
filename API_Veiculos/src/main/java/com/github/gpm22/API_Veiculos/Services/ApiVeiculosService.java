package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Client.ApiVeiculosClient;
import com.github.gpm22.API_Veiculos.Entities.Owner;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import com.github.gpm22.API_Veiculos.Repositories.OwnerRepository;
import com.github.gpm22.API_Veiculos.Repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class ApiVeiculosService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    ApiVeiculosClient client = new ApiVeiculosClient();

    public Vehicle save(String emailOuCpf, Vehicle vehicle) throws IllegalArgumentException {

        Owner owner = ownerRepository.findByCpfOrEmail(emailOuCpf, emailOuCpf);

        if (owner == null) {
            throw new IllegalArgumentException("Não existe usuário com o email ou cpf: " + emailOuCpf);
        }

        if (vehicle.getBrand().equals("") || vehicle.getBrand() == null) {
            throw new IllegalArgumentException("Parâmetro marca não pode ser vazio!");
        }

        if (vehicle.getModel().equals("") || vehicle.getModel() == null) {
            throw new IllegalArgumentException("Parâmetro modelo não pode ser vazio!");
        }

        if (vehicle.getYear().equals("") || vehicle.getYear() == null) {
            throw new IllegalArgumentException("Parâmetro ano não pode ser vazio!");
        }

        if (vehicle.getType().equals("") || vehicle.getType() == null) {
            throw new IllegalArgumentException("Parâmetro tipo não pode ser vazio!\n Deve ser: carros, motos ou caminhoes.");
        }

        vehicle.setRotationDay(this.rotationDay(vehicle.getYear()));
        vehicle.setRotationActive(this.isRotationActive(vehicle.getRotationDay()));
        vehicle.setPrice(this.getFipePrice(vehicle));
        owner.addVehicle(vehicle);

        return vehicleRepository.save(vehicle);
    }

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

    public int rotationDay(String year) {
        String lastDigit = year.substring(3, 4);

        return switch (lastDigit) {
            case "0", "1" -> Calendar.MONDAY;
            case "2", "3" -> Calendar.TUESDAY;
            case "4", "5" -> Calendar.WEDNESDAY;
            case "6", "7" -> Calendar.THURSDAY;
            case "8", "9" -> Calendar.FRIDAY;
            default -> Calendar.SUNDAY;
        };
    }

    public Boolean isRotationActive(int day) {
        return day == Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    public Boolean ownerNameValidation(String ownerName) {
        String nameValidation = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        return Pattern.compile(nameValidation).matcher(ownerName).matches();
    }

    public Boolean ownerEmailValidation(String ownerEmail) {
        String emailValidation = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailValidation).matcher(ownerEmail).matches();
    }

    public Boolean ownerCpfValidation(String ownerCpf) {
        String cpfValidation = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern.compile(cpfValidation).matcher(ownerCpf).matches();
    }

    public Boolean ownerBirthDateValidation(String ownerBirthDate) {
        String birthDateValidation = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

        return Pattern.compile(birthDateValidation).matcher(ownerBirthDate).matches();
    }

    private String getCodeBrand(String type, String vehicleBrand) {
        return client.getBrandList(type).filter(brand -> brand.getNome().equals(vehicleBrand)).findAny().get().getCodigo();
    }

    private String getCodeModel(String type, String codeBrand, String vehicleModel) {
        return Arrays.stream(client.getModelList(type, codeBrand)).sequential().filter(model -> model.getNome().equals(vehicleModel)).findAny().get().getCodigo();
    }

    private String getFipeYear(String type, String codeBrand, String codeModel, String vehicleYear) {
        return client.getYearlList(type, codeBrand, codeModel).filter(year -> year.getNome().equals(vehicleYear)).findAny().get().getCodigo();
    }

    public String getFipePrice(Vehicle vehicle) {

        String type = vehicle.getType();
        String codeBrand = getCodeBrand(type, vehicle.getBrand());
        String codeModel = getCodeModel(type, codeBrand, vehicle.getModel());
        String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear());

        return client.getFipePrice(type, codeBrand, codeModel, fipeYear).getValor();
    }

    public Set<Vehicle> getVehiclesByOwner(String emailOuCpf) {
        Optional<Owner> optional = Optional.ofNullable(ownerRepository.findByCpfOrEmail(emailOuCpf, emailOuCpf));

        if (optional.isPresent()) {
            Set<Vehicle> vehicles = optional.get().getVehicles();
            vehicles.forEach(n -> n.setRotationActive(this.isRotationActive(n.getRotationDay())));
            return vehicles;
        } else {
            throw new IllegalArgumentException("Não existe usuário com o " + (emailOuCpf.contains("@") ? "email" : "cpf") + ": " + emailOuCpf);
        }
    }
}

