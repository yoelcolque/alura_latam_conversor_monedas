package main.java.com.alura.api;

import java.util.Map;

public class ConversorMonedas {

    public double convertir(double monto, String monedaOrigen, String monedaDestino, Map<String, Double> tasas) {
        if (!tasas.containsKey(monedaOrigen) || !tasas.containsKey(monedaDestino)) {
            throw new IllegalArgumentException("Una o ambas monedas no están disponibles.");
        }

        double tasaOrigen = tasas.get(monedaOrigen);
        double tasaDestino = tasas.get(monedaDestino);

        return monto * (tasaDestino / tasaOrigen);
    }
}
