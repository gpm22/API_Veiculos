package com.github.gpm22.API_Veiculos.Clients.ApiFipe;

import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Brand;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Model;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.ModelYear;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Price;
import com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models.Year;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
public class ApiFipeClient {

        Logger logger = LoggerFactory.getLogger(ApiFipeClient.class);
        private final Duration TIME_OUT = Duration.ofSeconds(5);

        //TODO treat api call errors
        public Brand[] getBrandList(String type) {

            String uri = ApiFipeURL.getBrandURI(type);
            try {
                return getRetrieve(uri)
                           .bodyToMono(Brand[].class)
                           .block(TIME_OUT);
            } catch(RuntimeException e){
                throw handleRuntimeException(e);
            }
        }

        public Model[] getModelList(String type, String codeBrand) {

            String uri = ApiFipeURL.getModelURI(type, codeBrand);
            try {
                ModelYear modelYear = getRetrieve(uri)
                    .bodyToMono(ModelYear.class)
                    .block(TIME_OUT);

                return modelYear == null ? null : modelYear.getModelos();
            } catch(RuntimeException e){
                throw handleRuntimeException(e);
            }
        }

        public Year[] getYearList(String type, String codeBrand, String codeModel) {

            String uri = ApiFipeURL.getYearURI(type, codeBrand, codeModel);
            try{
                return getRetrieve(uri)
                    .bodyToMono(Year[].class)
                    .block(TIME_OUT);
            } catch(RuntimeException e){
                throw handleRuntimeException(e);
            }
        }

        public Price getFipePrice(String type, String codeBrand, String codeModel, String year) {

            String uri = ApiFipeURL.getPriceURI(type, codeBrand, codeModel, year);

            try {
                return getRetrieve(uri)
                    .bodyToMono(Price.class)
                    .block(TIME_OUT);
            } catch(RuntimeException e){
                throw handleRuntimeException(e);
            }
        }

        private WebClient.ResponseSpec getRetrieve(String uri) {

            return WebClient.create()
                       .method(HttpMethod.GET)
                       .uri(uri)
                       .retrieve()
                       .onStatus(HttpStatus::isError,
                                 response -> Mono.error(buildBadGatewayException(response, uri)));
        }

        private HttpServerErrorException buildBadGatewayException(ClientResponse response, String uri){
            String errorResponse = response.bodyToMono(String.class).block(TIME_OUT);
            String msg =  "Erro com código " + response.rawStatusCode()
                        + " ao chamar URI: " + uri
                        + ":\n" + errorResponse;
            return new HttpServerErrorException(HttpStatus.BAD_GATEWAY, msg);
        }

        private HttpServerErrorException handleRuntimeException(RuntimeException e){
            if (e.getCause().equals(new TimeoutException()))
                return new HttpServerErrorException(HttpStatus.GATEWAY_TIMEOUT);

            if(e instanceof HttpServerErrorException)
                return (HttpServerErrorException) e;

            logger.error(e.getMessage());
            return new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
