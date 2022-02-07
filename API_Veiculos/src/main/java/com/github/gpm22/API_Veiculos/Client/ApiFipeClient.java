package com.github.gpm22.API_Veiculos.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gpm22.API_Veiculos.Client.Models.Brand;
import com.github.gpm22.API_Veiculos.Client.Models.Model;
import com.github.gpm22.API_Veiculos.Client.Models.ModelYear;
import com.github.gpm22.API_Veiculos.Client.Models.Price;
import com.github.gpm22.API_Veiculos.Client.Models.Year;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class ApiFipeClient {

    private final String URI_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public Stream<Brand> getBrandList(String type) {

        ObjectMapper mapper = new ObjectMapper();

        return Arrays
                .stream(getRetrieve(URI_BASE +type+"/marcas")
                        .bodyToMono(Brand[].class)
                        .block())
                .map(value -> mapper.convertValue(value, Brand.class));
    }

    public Stream<Model> getModelList(String type, String codeBrand) {

        return Arrays.stream(getRetrieve(URI_BASE +type+"/marcas/"+codeBrand+"/modelos")
                .bodyToMono(ModelYear.class)
                .block()
                .getModelos());

    }

    public Stream<Year> getYearlList(String type, String codeBrand, String codeModel) {

        ObjectMapper mapper = new ObjectMapper();
        return Arrays
                .stream(getRetrieve(URI_BASE +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos")
                        .bodyToMono(Year[].class)
                        .block())
                .map(value -> mapper.convertValue(value, Year.class));
    }

    public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

        return getRetrieve(URI_BASE +type+"/marcas/"+codeBrand+"/modelos/"+codeModel+"/anos/"+year)
                .bodyToMono(Price.class)
                .block();
    }

    private WebClient.ResponseSpec getRetrieve(String uri){
        return WebClient
                .create()
                .method(HttpMethod.GET)
                .uri(uri)
                .retrieve();
    }
}
