import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CarroDtoInput;
import org.example.dto.CarroDtoOutput;
import org.example.service.CarroService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CarroServiceTest {

    private final CarroService carroService = new CarroService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInserirCarro() {
        CarroDtoInput carroDtoInput = new CarroDtoInput();
        carroDtoInput.setModelo("gol");
        carroDtoInput.setPlaca("abc1234");
        carroDtoInput.setChassi("123231412131");
        carroService.inserir(carroDtoInput);
        List<CarroDtoOutput> carrosListados = carroService.listar();
        assertEquals(1, carrosListados.size(), "A lista deve ter 1 carro após a inserção");
        CarroDtoOutput carroInserido = carrosListados.get(0);
        assertEquals("gol", carroInserido.getModelo());
        assertEquals("abc1234", carroInserido.getPlaca());
        System.out.println("Modelo do carro inserido: " + carroInserido.getModelo());
    }

    @Test
    void testListagemCarros() throws IOException {
        URL url = new URL("http://localhost:4567/carros");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        assertEquals(200, responseCode, "A resposta da api deve ser 200");
    }

    @Test
    void testInsercaoCarroFreeTestApiComJsonObject() {
        JSONArray carroJsonArr = fazerRequestComJsonObject("https://freetestapi.com/api/v1/cars");
        JSONObject carro = carroJsonArr.getJSONObject(0); //pega o primeiro
        String modelo = carro.getString("model");
        String placa = gerarPlacaAleatoria();
        String chassi = gerarChassiAleatorio();
        JSONObject carroJson = new JSONObject();
        carroJson.put("modelo", modelo);
        carroJson.put("placa", placa);
        carroJson.put("chassi", chassi);
        System.out.println(carroJson);
        int resposta = fazerPostComJsonObject("http://localhost:4567/carros", carroJson);
        assertEquals(201, resposta, "A inserção deve retornar 201");
    }

    public static JSONArray fazerRequestComJsonObject(String urlApiExterna) {
        try {
            URL url = new URL(urlApiExterna);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                } in .close();
                return new JSONArray(response.toString());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    public static int fazerPostComJsonObject(String urlApiInterna, JSONObject jsonObj) {
        try {
            URL url = new URL(urlApiInterna);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            con.getOutputStream().write(jsonObj.toString().getBytes());
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Resposta api: " + response);
            }
            return responseCode;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    @Test
    void testInsercaoCarroFreeTestApiComObjectMapper() {
        CarroDtoInput carro = fazerRequestComObjectMapper("https://freetestapi.com/api/v1/cars/1");
        String placa = gerarPlacaAleatoria();
        String chassi = gerarChassiAleatorio();
        carro.setId(0);
        carro.setPlaca(placa);
        carro.setChassi(chassi);
        int resposta = fazerPostComObjectMapper("http://localhost:4567/carros", carro);
        assertEquals(201, resposta, "A inserção deve retornar 201");
    }

    public CarroDtoInput fazerRequestComObjectMapper(String urlApiExterna){
        try{
            URL url = new URL(urlApiExterna);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return objectMapper.readValue(response.toString(), objectMapper.getTypeFactory().constructType(CarroDtoInput.class));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    public int fazerPostComObjectMapper(String urlApiInterna, CarroDtoInput carroDto) {
        try {
            URL url = new URL(urlApiInterna);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String json = objectMapper.writeValueAsString(carroDto);
            con.getOutputStream().write(json.getBytes());
            System.out.println(carroDto);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("Resposta api: " + response.toString());
            }
            return responseCode;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    private static String gerarChassiAleatorio() {
        Random random = new Random();
        StringBuilder chassi = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int numAleatorio = random.nextInt(10);
            chassi.append(numAleatorio);
        }
        return chassi.toString();
    }

    private static String gerarPlacaAleatoria() {
        //aaa1a11
        Random random = new Random();
        StringBuilder placa = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char letraAleatoria = (char) (random.nextInt(26) + 'a');
            placa.append(letraAleatoria);
        }
        placa.append(random.nextInt(10));
        char letraAleatoria = (char) (random.nextInt(26) + 'a');
        placa.append(letraAleatoria);
        for (int i = 0; i < 2; i++) {
            int numAleatorio = random.nextInt(10);
            placa.append(numAleatorio);
        }
        return placa.toString();
    }
}