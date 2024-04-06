package com.github.gpm22.API_Veiculos.Clients.ApiFipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.ModelYear;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Price;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Year;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class ApiFipeClient {

        private final ObjectMapper mapper = new ObjectMapper();

        public Stream<Brand> getBrandList(String type) {

                String uri = ApiFipeURL.getBrandURI(type);
                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(Brand[].class)
                                .block())
                                .map(value -> mapper.convertValue(value, Brand.class));
        }

        public Stream<Model> getModelList(String type, String codeBrand) {

                String uri = ApiFipeURL.getModelURI(type, codeBrand);
                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(ModelYear.class)
                                .block()
                                .getModelos());

        }

        public Stream<Year> getYearList(String type, String codeBrand, String codeModel) {

                String uri = ApiFipeURL.getYearURI(type, codeBrand, codeModel);
                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(Year[].class)
                                .block())
                                .map(value -> mapper.convertValue(value, Year.class));
        }

        public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

                String uri = ApiFipeURL.getPriceURI(type, codeBrand, codeModel, year);

                return getRetrieve(uri)
                                .bodyToMono(Price.class)
                                .block();
        }

        private WebClient.ResponseSpec getRetrieve(String uri) {
                return WebClient.create()
                                .method(HttpMethod.GET)
                                .uri(uri)
                                .retrieve();
        }
}
