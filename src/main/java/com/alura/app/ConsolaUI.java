package main.java.com.alura.app;

import main.java.com.alura.api.ConversorMonedas;
import main.java.com.alura.api.GestorTasas;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConsolaUI implements ConversorUI {

    private static final List<String> MONEDAS = Arrays.asList("USD", "ARS", "BRL", "CLP", "COP", "BOB");

    @Override
    public void iniciar() {
        GestorTasas gestor = new GestorTasas();
        Map<String, Double> tasas = gestor.obtenerTasas();

        if (tasas == null) {
            System.out.println("❌ No se pudo cargar la información de tasas. Saliendo...");
            return;
        }

        System.out.println("📅 Última actualización: " +
                gestor.getFechaUltimaActualizacion().format(DateTimeFormatter.ISO_LOCAL_DATE));

        ConversorMonedas conversor = new ConversorMonedas();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n💱 Bienvenido/a al Conversor de Monedas");
            System.out.println("Seleccione la moneda de origen:");
            mostrarOpciones();

            int origen = leerOpcion(scanner);
            if (origen == 0) break;

            System.out.println("Seleccione la moneda de destino:");
            mostrarOpciones();

            int destino = leerOpcion(scanner);
            if (destino == 0) break;

            System.out.print("Ingrese el monto a convertir: ");
            double monto = scanner.nextDouble();

            try {
                String codOrigen = MONEDAS.get(origen - 1);
                String codDestino = MONEDAS.get(destino - 1);

                double resultado = conversor.convertir(monto, codOrigen, codDestino, tasas);

                System.out.printf("💸 %.2f %s equivalen a → %.2f %s ✅\n",
                        monto, codOrigen, resultado, codDestino);

            } catch (Exception e) {
                System.out.println("❌ Error en la conversión: " + e.getMessage());
            }

            System.out.println("\n¿Desea realizar otra conversión? (s/n)");
            String seguir = scanner.next();
            if (!seguir.equalsIgnoreCase("s")) break;
        }

        System.out.println("¡Gracias por usar el conversor!");
    }

    private void mostrarOpciones() {
        for (int i = 0; i < MONEDAS.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, MONEDAS.get(i));
        }
        System.out.println("0. Salir");
    }

    private int leerOpcion(Scanner scanner) {
        System.out.print("Ingrese una opción: ");
        int opcion = scanner.nextInt();
        if (opcion < 0 || opcion > MONEDAS.size()) {
            System.out.println("Opción inválida.");
            return 0;
        }
        return opcion;
    }
}
