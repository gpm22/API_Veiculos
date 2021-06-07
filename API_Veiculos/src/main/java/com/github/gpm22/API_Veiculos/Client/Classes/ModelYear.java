package com.github.gpm22.API_Veiculos.Client.Classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelYear {
    private Year[] anos;
    private Model[] modelos;

    public Year[] getAnos() {
        return anos;
    }

    public void setAnos(Year[] anos) {
        this.anos = anos;
    }

    public Model[] getModelos() {
        return modelos;
    }

    public void setModelos(Model[] modelos) {
        this.modelos = modelos;
    }
}
