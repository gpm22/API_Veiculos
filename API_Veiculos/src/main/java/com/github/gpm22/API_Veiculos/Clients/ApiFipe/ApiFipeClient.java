package com.github.gpm22.API_Veiculos.Clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gpm22.API_Veiculos.Clients.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.Models.ModelYear;
import com.github.gpm22.API_Veiculos.Clients.Models.Price;
import com.github.gpm22.API_Veiculos.Clients.Models.Year;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.stream.Stream;

@Component
public class ApiFipeClient {

        @Value("${client.url.fipe.api.base}")
        private String URI_BASE;

        @Value("${client.url.fipe.api.brand}")
        private String BRAND_PATH;

        @Value("${client.url.fipe.api.model}")
        private String MODEL_PATH;

        @Value("${client.url.fipe.api.year}")
        private String YEAR_PATH;

        private final ObjectMapper mapper = new ObjectMapper();

        public Stream<Brand> getBrandList(String type) {

                String uri = URI_BASE + "/" + type + "/" + BRAND_PATH;
                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(Brand[].class)
                                .block())
                                .map(value -> mapper.convertValue(value, Brand.class));
        }

        public Stream<Model> getModelList(String type, String codeBrand) {

                String uri = URI_BASE + "/" + type
                                + "/" + BRAND_PATH + "/" + codeBrand
                                + "/" + MODEL_PATH;
                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(ModelYear.class)
                                .block()
                                .getModelos());

        }

        public Stream<Year> getYearlList(String type, String codeBrand, String codeModel) {

                String uri = URI_BASE + "/" + type + "/" + BRAND_PATH +
                                "/" + codeBrand + "/" + MODEL_PATH
                                + "/" + codeModel + "/" + YEAR_PATH;

                return Arrays.stream(getRetrieve(uri)
                                .bodyToMono(Year[].class)
                                .block())
                                .map(value -> mapper.convertValue(value, Year.class));
        }

        public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

                String uri = URI_BASE + "/" + type + "/" + BRAND_PATH
                                + "/" + codeBrand + "/" + MODEL_PATH
                                + "/" + codeModel + "/" + YEAR_PATH
                                + "/" + year;

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
