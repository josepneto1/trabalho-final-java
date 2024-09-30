package org.example.service;

import org.example.dto.CarroDtoInput;
import org.example.dto.CarroDtoOutput;
import org.example.model.Carro;
import org.example.repository.CarroRepository;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CarroService {
    private final CarroRepository carroRepository = new CarroRepository();
    private final ModelMapper modelMapper = new ModelMapper();

    public List<CarroDtoOutput> listar() {
        List<Carro> carros = carroRepository.listar();
        List<CarroDtoOutput> carrosDto = carros.stream()
                .map(c -> modelMapper.map(c, CarroDtoOutput.class))
                .collect(Collectors.toList());
        return carrosDto;
    }

    public CarroDtoOutput buscar(int id) {
        Carro carro = carroRepository.buscar(id);
        if (carro == null) {
            return null;
        }
        return modelMapper.map(carro, CarroDtoOutput.class);
    }

    public void inserir(CarroDtoInput carroDtoInput) {
        Carro carro = modelMapper.map(carroDtoInput, Carro.class);
        carroRepository.inserir(carro);
    }

    public void alterar(CarroDtoInput carroDtoInput, int id) {
        Carro carro = modelMapper.map(carroDtoInput, Carro.class);
        carro.setId(id);
        carroRepository.alterar(carro);
    }

    public void excluir(int id) {
        Carro carro = carroRepository.buscar(id);
        if (carro == null) {
            return;
        }
        carroRepository.excluir(carro);
    }
}
