package com.github.gpm22.API_Veiculos.Clients.ApiFipe;

import java.util.Arrays;
import java.util.List;

public class ApiFipeURL {

    public final static List<String> VEHICLE_TYPES = Arrays.asList("carros", "motos", "caminhoes", "fipe");

    private final static String URI_BASE = "https://parallelum.com.br/fipe/api/v1";

    private final static String BRAND_PATH = "marcas";

    private final static String MODEL_PATH = "modelos";

    private final static String YEAR_PATH = "anos";

    public static String getBrandURI(String type){
        return URI_BASE + "/" + type + "/" + BRAND_PATH;
    }

    public static String getModelURI(String type, String codeBrand){
        return getBrandURI(type) + "/" + codeBrand + "/" + MODEL_PATH;
    }

    public static String getYearURI(String type, String codeBrand, String codeModel){
        return getModelURI(type, codeBrand) + "/" + codeModel + "/" + YEAR_PATH;
    }

    public static String getPriceURI(String type, String codeBrand, String codeModel, String year){
        return getYearURI(type, codeBrand, codeModel) + "/" + year;
    }
}
