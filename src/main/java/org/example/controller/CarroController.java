package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarroDtoInput;
import org.example.dto.CarroDtoOutput;
import org.example.service.CarroService;

import static spark.Spark.*;

public class CarroController {
    private final CarroService carroService = new CarroService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void respostasRequisicoes() {
        get("/carros", (request, response) -> {
            response.type("application/json");
            response.status(200);
            return objectMapper.writeValueAsString(carroService.listar());
        });

        get("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            res.type("application/json");
            CarroDtoOutput carro = carroService.buscar(id);
            if (carro != null) {
                res.status(200);
                return objectMapper.writeValueAsString(carro);
            } else {
                res.status(404);
                return "Erro: Carro não encontrado";
            }
        });

        post("/carros", (request, response) -> {
            CarroDtoInput carroInput = objectMapper.readValue(request.body(), CarroDtoInput.class);
            carroService.inserir(carroInput);
            response.status(201);
            return "Carro cadastrado";
        });

        put("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            CarroDtoInput carroInput = objectMapper.readValue(req.body(), CarroDtoInput.class);
            carroService.alterar(carroInput, id);
            res.status(200);
            return "Carro foi alterado";
        });

        delete("/carros/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            carroService.excluir(id);
            res.status(204);
            return "Carro excluído";
        });
    }
}
