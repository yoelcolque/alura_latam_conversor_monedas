package main.java.com.alura.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GestorTasas {

    private static final String ARCHIVO_CACHE = "data/tasas.json";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String ENDPOINT = "/latest/USD";
    private static final Gson gson = new Gson();

    private Map<String, Double> tasas;
    private LocalDate ultimaFecha;

    public GestorTasas() {
        cargarOActualizarCache();
    }

    public Map<String, Double> obtenerTasas() {
        return tasas;
    }

    public LocalDate getFechaUltimaActualizacion() {
        return ultimaFecha;
    }

    private void cargarOActualizarCache() {
        File archivo = new File(ARCHIVO_CACHE);

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("📂 Cache no encontrado o vacío. Obteniendo tasas...");
            actualizarCacheDesdeAPI();
            return;
        }

        try {
            String contenido = Files.readString(Paths.get(ARCHIVO_CACHE));
            JsonObject json = JsonParser.parseString(contenido).getAsJsonObject();

            String fechaStr = json.get("ultima_actualizacion").getAsString();
            ultimaFecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE);

            if (ultimaFecha.isBefore(LocalDate.now())) {
                System.out.println("🔁 Cache vencido. Actualizando...");
                actualizarCacheDesdeAPI();
                return;
            }

            JsonObject conversiones = json.getAsJsonObject("conversion_rates");
            tasas = gson.fromJson(conversiones, Map.class);
            System.out.println("✅ Cache cargado desde archivo.");

        } catch (Exception e) {
            System.out.println("⚠️ Error leyendo cache. Reintentando desde la API...");
            actualizarCacheDesdeAPI();
        }
    }

    private void actualizarCacheDesdeAPI() {
        String apiKey = ApiKeyReader.leer(".env");
        if (apiKey == null) return;

        try {
            ExchangeRateClient client = new ExchangeRateClient();
            Map<String, Double> nuevasTasas = client.obtenerTasasDesdeUSD();
            if (nuevasTasas == null) {
                System.out.println("❌ Error al actualizar tasas desde API.");
                return;
            }

            this.tasas = nuevasTasas;
            this.ultimaFecha = LocalDate.now();

            JsonObject jsonGuardar = new JsonObject();
            jsonGuardar.addProperty("ultima_actualizacion", ultimaFecha.toString());
            jsonGuardar.add("conversion_rates", gson.toJsonTree(nuevasTasas));

            try (FileWriter writer = new FileWriter(ARCHIVO_CACHE)) {
                gson.toJson(jsonGuardar, writer);
            }

            System.out.println("📥 Tasas actualizadas y guardadas en cache.");

        } catch (Exception e) {
            System.out.println("⚠️ Error al obtener tasas desde API: " + e.getMessage());
        }
    }
}
