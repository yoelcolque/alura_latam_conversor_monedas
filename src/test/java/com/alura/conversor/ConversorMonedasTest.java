package test.java.com.alura.conversor;

import main.java.com.alura.api.ConversorMonedas;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConversorMonedasTest {

    @Test
    void debeConvertirCorrectamenteEntreMonedas() {
        ConversorMonedas conversor = new ConversorMonedas();
        Map<String, Double> tasas = Map.of(
                "USD", 1.0,
                "ARS", 857.0,
                "BRL", 5.13
        );

        double monto = 100.0;
        double esperado = monto * (857.0 / 5.13);
        double resultado = conversor.convertir(monto, "BRL", "ARS", tasas);

        assertEquals(esperado, resultado, 0.01);
    }

    @Test
    void debeRetornarElMismoMontoSiOrigenYDestinoSonIguales() {
        ConversorMonedas conversor = new ConversorMonedas();
        Map<String, Double> tasas = Map.of("USD", 1.0);

        double monto = 50.0;
        double resultado = conversor.convertir(monto, "USD", "USD", tasas);

        assertEquals(monto, resultado, 0.0001);
    }

    @Test
    void debeLanzarExcepcionSiMonedaNoExiste() {
        ConversorMonedas conversor = new ConversorMonedas();
        Map<String, Double> tasas = Map.of("USD", 1.0);

        try {
            conversor.convertir(100, "USD", "ZZZ", tasas);
        } catch (IllegalArgumentException e) {
            assertEquals("Una o ambas monedas no están disponibles.", e.getMessage());
        }
    }
}
