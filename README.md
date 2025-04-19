# Conversor de Monedas

Este proyecto es una aplicación de consola escrita en Java que permite convertir valores entre diferentes monedas utilizando una API de tasas de cambio en tiempo real.

## Características principales

- Conversión entre monedas como USD, ARS, BRL, CLP, BOB, COP.
- Consumo de la API ExchangeRate para obtener datos reales.
- Sistema de caché con validación de fecha para evitar consultas innecesarias.
- Lectura de clave API desde archivo `.env`.
- Interfaz de usuario por consola con bucle interactivo.
- Implementación basada en principios de programación orientada a objetos.
- Manejo de errores ante formatos inválidos, archivos corruptos o tasas vencidas.
- Pruebas unitarias completas sobre la lógica de conversión y persistencia local.

## Estructura lógica

- **ConversorMonedas**: calcula la conversión usando tasas.
- **ExchangeRateClient**: consume la API de cambio de divisas.
- **GestorTasas**: maneja el archivo local con las tasas y verifica vencimiento.
- **ApiKeyReader**: extrae la clave desde un archivo `.env`.
- **ConsolaUI/Main**: interfaz por consola para que el usuario interactúe.

## Tests

Se implementaron pruebas unitarias para validar los siguientes componentes:

- Correcta conversión entre monedas conocidas.
- Manejo de errores por monedas inexistentes.
- Generación, lectura y regeneración del archivo de tasas (`tasas.json`).
- Validación de fechas y archivo corrupto.

Se decidió omitir pruebas con `Mockito` por razones de compatibilidad con el entorno actual.

## Optimización mediante sistema de caché

Para evitar múltiples llamadas innecesarias a la API y 
considerando que el plan gratuito permite
solo 1500 consultas al mes (con actualizaciones cada 24 hs),
se implementó un sistema de caché diario.
Esto permite que múltiples conversiones usen un único conjunto de tasas por día.

A continuación se visualiza la diferencia acumulada
de llamadas entre un sistema sin caché (consulta directa por cada conversión)
y el sistema con caché diario:
![Comparación cache vs directo](docs/grad.png)

---
