package test.java.com.alura.conversor;

import main.java.com.alura.api.GestorTasas;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GestorTasasTest {

    private static final String ARCHIVO_CACHE = "tasas.json";

    @BeforeEach
    void limpiarArchivoAntes() {
        File f = new File(ARCHIVO_CACHE);
        if (f.exists()) f.delete();
    }

    @AfterEach
    void limpiarArchivoDespues() {
        File f = new File(ARCHIVO_CACHE);
        if (f.exists()) f.delete();
    }

    @Test
    void debeCrearArchivoCacheSiNoExiste() {
        assertFalse(new File(ARCHIVO_CACHE).exists());
        GestorTasas gestor = new GestorTasas();
        assertTrue(new File(ARCHIVO_CACHE).exists());
    }

    @Test
    void debeLeerArchivoCacheSiYaExisteYEstaActualizado() throws Exception {
        String contenido =
                "{\n" +
                        "  \\\"ultima_actualizacion\\\": \\\"" + LocalDate.now() + "\\\",\n" +
                        "  \\\"conversion_rates\\\": {\n" +
                        "    \\\"USD\\\": 1.0,\n" +
                        "    \\\"ARS\\\": 857.0\n" +
                        "  }\n" +
                        "}";

        try (FileWriter writer = new FileWriter(ARCHIVO_CACHE)) {
            writer.write(contenido);
        }

        GestorTasas gestor = new GestorTasas();
        Map<String, Double> tasas = gestor.obtenerTasas();

        assertNotNull(tasas);
        assertEquals(857.0, tasas.get("ARS"));
        assertEquals(LocalDate.now(), gestor.getFechaUltimaActualizacion());
    }

    @Test
    void debeIgnorarArchivoVencidoYReemplazarlo() throws Exception {
        String contenido =
                "{\n" +
                        "  \\\"ultima_actualizacion\\\": \\\"2020-01-01\\\",\n" +
                        "  \\\"conversion_rates\\\": {\n" +
                        "    \\\"USD\\\": 1.0,\n" +
                        "    \\\"ARS\\\": 500.0\n" +
                        "  }\n" +
                        "}";

        try (FileWriter writer = new FileWriter(ARCHIVO_CACHE)) {
            writer.write(contenido);
        }

        GestorTasas gestor = new GestorTasas();
        Map<String, Double> tasas = gestor.obtenerTasas();

        assertNotNull(tasas);
        assertNotEquals(500.0, tasas.get("ARS"));
        assertEquals(LocalDate.now(), gestor.getFechaUltimaActualizacion());
    }

    @Test
    void debeRegenerarArchivoSiEstaCorrupto() throws Exception {
        try (FileWriter writer = new FileWriter(ARCHIVO_CACHE)) {
            writer.write("{esto no es JSON valido");
        }

        GestorTasas gestor = new GestorTasas();
        Map<String, Double> tasas = gestor.obtenerTasas();

        assertNotNull(tasas);
        assertTrue(tasas.containsKey("USD"));
    }
}