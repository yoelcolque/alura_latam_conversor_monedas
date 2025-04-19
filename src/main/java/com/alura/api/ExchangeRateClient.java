package main.java.com.alura.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ExchangeRateClient {

    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String ENDPOINT = "/latest/USD";
    private static final Gson gson = new Gson();

    public Map<String, Double> obtenerTasasDesdeUSD() {
        String apiKey = ApiKeyReader.leer(".env");
        if (apiKey == null) {
            System.out.println("❌ API Key no válida.");
            return null;
        }

        String url = BASE_URL + apiKey + ENDPOINT;

        try {
            // Construir la petición HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Ejecutar y capturar la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parsear el JSON
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            if (!json.get("result").getAsString().equals("success")) {
                System.out.println("⚠️ La API respondió con error.");
                return null;
            }

            JsonObject conversiones = json.getAsJsonObject("conversion_rates");

            return gson.fromJson(conversiones, Map.class);

        } catch (Exception e) {
            System.out.println("⚠️ Error al obtener datos de la API: " + e.getMessage());
            return null;
        }
    }
}
