package com.github.gpm22.API_Veiculos.Clients.ApiFipe.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Price {
    private String valor;

    public String getValor() {
        return valor;
    }

    @JsonProperty("Valor")
    public void setValor(String Valor) {
        this.valor = Valor;
    }
}
