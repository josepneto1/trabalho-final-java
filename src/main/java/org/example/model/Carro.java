package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Carro {
    @Id
    @GeneratedValue
    private int id;
    private String modelo;
    private String placa;
    private String chassi;

    @Override
    public String toString() {
        return id + " " + modelo + " " + placa + " " + chassi;
    }
}
