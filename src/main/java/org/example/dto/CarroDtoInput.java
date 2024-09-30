package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarroDtoInput {
    private int id;
    @JsonProperty("model")
    private String modelo;
    private String placa;
    private String chassi;

    @Override
    public String toString() {
        return id + " " + modelo + " " + placa + " " + chassi;
    }
}
