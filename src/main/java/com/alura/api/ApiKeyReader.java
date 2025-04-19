package main.java.com.alura.api;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiKeyReader {
    public static String leer(String ruta) {
        try {
            return Files.readString(Paths.get(ruta)).trim();
        } catch (Exception e) {
            System.out.println("❌ No se pudo leer la API Key.");
            return null;
        }
    }
}
