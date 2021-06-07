package com.github.gpm22.API_Veiculos.Services;

import com.github.gpm22.API_Veiculos.Client.ApiVeiculosClient;
import com.github.gpm22.API_Veiculos.Entities.Vehicle;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Pattern;

@Service
public class ApiVeiculosService{

    ApiVeiculosClient client = new ApiVeiculosClient();

    public int rotationDay(String year){
        String lastDigit = year.substring(3,4);

        return switch (lastDigit) {
            case "0" -> Calendar.MONDAY;
            case "1" -> Calendar.MONDAY;
            case "2" -> Calendar.TUESDAY;
            case "3" -> Calendar.TUESDAY;
            case "4" -> Calendar.WEDNESDAY;
            case "5" -> Calendar.WEDNESDAY;
            case "6" -> Calendar.THURSDAY;
            case "7" -> Calendar.THURSDAY;
            case "8" -> Calendar.FRIDAY;
            case "9" -> Calendar.FRIDAY;
            default -> Calendar.SUNDAY;
        };
    }

    public Boolean isRotationActive(int day){
        return day == Calendar
                .getInstance()
                .get(Calendar.DAY_OF_WEEK);
    }

    public Boolean ownerNameValidation(String ownerName) {
        String nameValidation = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        return Pattern
                .compile(nameValidation)
                .matcher(ownerName)
                .matches();
    }

    public Boolean ownerEmailValidation(String ownerEmail) {
        String emailValidation = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern
                .compile(emailValidation)
                .matcher(ownerEmail)
                .matches();
    }

    public Boolean ownerCpfValidation(String ownerCpf) {
        String cpfValidation = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";

        return Pattern
                .compile(cpfValidation)
                .matcher(ownerCpf)
                .matches();
    }

    public Boolean ownerBirthDateValidation(String ownerBirthDate) {
        String birthDateValidation="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

        return Pattern
                .compile(birthDateValidation)
                .matcher(ownerBirthDate)
                .matches();
    }

    private String getCodeBrand(String type, String vehicleBrand){
        return client
                .getBrandList(type)
                .filter(brand -> brand.getNome().equals(vehicleBrand))
                .findAny()
                .get()
                .getCodigo();
    }

    private String getCodeModel(String type, String codeBrand , String vehicleModel){
        return Arrays
                .stream(client
                        .getModelList(type, codeBrand))
                .sequential().filter(model -> model.getNome().equals(vehicleModel))
                .findAny()
                .get()
                .getCodigo();
    }

    private String getFipeYear(String type, String codeBrand , String codeModel , String vehicleYear){

        return client
                .getYearlList(type, codeBrand, codeModel)
                .filter(year -> year.getNome().equals(vehicleYear))
                .findAny()
                .get()
                .getCodigo();
    }

    public String getFipePrice(Vehicle vehicle){

        String type = vehicle.getType();
        String codeBrand = getCodeBrand(type, vehicle.getBrand());
        String codeModel = getCodeModel(type, codeBrand, vehicle.getModel());
        String fipeYear = getFipeYear(type, codeBrand, codeModel, vehicle.getYear());

        return client
                .getFipePrice(type, codeBrand, codeModel, fipeYear)
                .getValor();
    }



}

