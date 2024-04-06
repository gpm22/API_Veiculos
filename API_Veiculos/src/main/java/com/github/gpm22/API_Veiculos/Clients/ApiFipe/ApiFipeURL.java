package com.github.gpm22.API_Veiculos.Clients.ApiFipe;

import org.springframework.beans.factory.annotation.Value;

public class ApiFipeURL {

    @Value("${client.url.fipe.api.base}")
    private static String URI_BASE;

    @Value("${client.url.fipe.api.brand}")
    private static String BRAND_PATH;

    @Value("${client.url.fipe.api.model}")
    private static String MODEL_PATH;

    @Value("${client.url.fipe.api.year}")
    private static String YEAR_PATH;

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
