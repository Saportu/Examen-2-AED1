# Examen-2-AED1
Versión B — **Salida Segura**. Java 17+, Gradle, JUnit 5. Prompts de IA en [PROMPTS.md](PROMPTS.md).

| Feature | Estructura / algoritmo | Clases |
|---|---|---|
| F1 Directorio del edificio | Árbol n-ario, actualización O(h) | `ArbolEdificio`, `Nodo` |
| F2 Reporte para la brigada | Radix sort LSD, O(n) | `RadixSort` |
| F3 Guía de evacuación | Dijkstra con arreglos, O(V²), doble sentido | `Grafo`, `Dijkstra` |

Datos: `src/main/resources/salas.csv` y `pasillos.csv`.

```bash
gradle test   # pruebas
gradle run    # personas por ala/edificio, reporte de brigada, guía de las 6 salas
```
