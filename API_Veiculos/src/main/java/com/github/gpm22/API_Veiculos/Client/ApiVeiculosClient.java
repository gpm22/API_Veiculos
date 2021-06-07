package com.github.gpm22.API_Veiculos.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gpm22.API_Veiculos.Client.Classes.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Stream;

public class ApiVeiculosClient {

    private final String uri_base = "https://parallelum.com.br/fipe/api/v1/";

    private WebClient.ResponseSpec getRetrieve(String uri){
        return WebClient
                .create()
                .method(HttpMethod.GET)
                .uri(uri)
                .retrieve();
    }

    public Stream<Brand> getBrandList(String type) {

        ObjectMapper mapper = new ObjectMapper();

        return Arrays
                .stream(getRetrieve(uri_base +type+"/marcas")
                        .bodyToMono(Brand[].class)
                        .block())
                .map(value -> mapper.convertValue(value, Brand.class));
    }

    public Model[] getModelList(String type, String codeBrand) {

        return getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos")
                .bodyToMono(ModelYear.class)
                .block()
                .getModelos();

    }

    public Stream<Year> getYearlList(String type, String codeBrand, String codeModel) {

        ObjectMapper mapper = new ObjectMapper();
        return Arrays
                .stream(getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos")
                        .bodyToMono(Year[].class)
                        .block())
                .map(value -> mapper.convertValue(value, Year.class));
    }

    public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

        return getRetrieve(uri_base +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos/"+year)
                .bodyToMono(Price.class)
                .block();
    }

}
