package com.github.gpm22.API_Veiculos.Clients.ApiFipe;

import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.ModelYear;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Price;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Year;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ApiFipeClient {
        //TODO treat api call errors
        public Brand[] getBrandList(String type) {

                String uri = ApiFipeURL.getBrandURI(type);
                return getRetrieve(uri)
                        .bodyToMono(Brand[].class)
                        .block();
        }

        public Model[] getModelList(String type, String codeBrand) {

                String uri = ApiFipeURL.getModelURI(type, codeBrand);
                ModelYear modelYear = getRetrieve(uri)
                        .bodyToMono(ModelYear.class)
                        .block();

                return modelYear.getModelos();
        }

        public Year[] getYearList(String type, String codeBrand, String codeModel) {

                String uri = ApiFipeURL.getYearURI(type, codeBrand, codeModel);
                return getRetrieve(uri)
                        .bodyToMono(Year[].class)
                        .block();
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
