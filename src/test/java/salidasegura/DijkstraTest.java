package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** F3 — Guía de evacuación (Dijkstra O(V²), pasillos en ambos sentidos). */
class DijkstraTest {

    private static final String[] SEIS_SALAS = { "LAB-101", "LAB-102", "RM-103", "RM-104", "OFF-105", "LIB-001" };

    private Grafo grafo;
    private Dijkstra guia;

    @BeforeEach
    void cargar() {
        grafo = new Grafo(CargadorCsv.pasillos("/pasillos.csv"));
        guia = new Dijkstra(grafo, Dijkstra.SALIDAS);
    }

    // ===== RF-01: para cada sala indica qué salida usar y qué pasillos seguir =====

    @Test
    void rf01ValorExactoRutaDeLasSeisSalas() {
        assertArrayEquals(new String[] { "LAB-101", "Pasillo A", "Salida Norte" }, guia.ruta("LAB-101"));
        assertArrayEquals(new String[] { "LAB-102", "Pasillo A", "Salida Norte" }, guia.ruta("LAB-102"));
        assertArrayEquals(new String[] { "RM-103", "RM-104", "Pasillo C", "Salida Sur" }, guia.ruta("RM-103"));
        assertArrayEquals(new String[] { "RM-104", "Pasillo C", "Salida Sur" }, guia.ruta("RM-104"));
        assertArrayEquals(new String[] { "OFF-105", "Pasillo C", "Salida Sur" }, guia.ruta("OFF-105"));
        assertArrayEquals(new String[] { "LIB-001", "Salida Este" }, guia.ruta("LIB-001"));
    }

    @Test
    void rf01ValorExactoSalidaDeCadaSala() {
        assertEquals("Salida Norte", guia.salida("LAB-101"));
        assertEquals("Salida Norte", guia.salida("LAB-102"));
        assertEquals("Salida Sur", guia.salida("RM-103"));
        assertEquals("Salida Sur", guia.salida("RM-104"));
        assertEquals("Salida Sur", guia.salida("OFF-105"));
        assertEquals("Salida Este", guia.salida("LIB-001"));
    }

    @Test
    void rf01LimiteOrigenIgualAlDestino() {
        assertArrayEquals(new String[] { "Salida Norte" }, guia.ruta("Salida Norte"));
        assertEquals("Salida Norte", guia.salida("Salida Norte"));
    }

    @Test
    void rf01LimiteEmpateEntreDosSalidas() {
        Pasillo[] p = {
            new Pasillo("Sala T", "Salida Norte", 5, "Abierto"),
            new Pasillo("Sala T", "Salida Sur", 5, "Abierto"),
        };
        Dijkstra d = new Dijkstra(new Grafo(p), Dijkstra.SALIDAS);
        assertEquals(5, d.segundos("Sala T"));
        assertEquals(2, d.ruta("Sala T").length);
        // Con empate se usa la primera salida alcanzada (orden de los pasillos).
        assertEquals("Salida Norte", d.salida("Sala T"));
    }

    @Test
    void rf01LimiteSinRutaDisponible() {
        Pasillo[] p = {
            new Pasillo("Sala X", "Pasillo Z", 5, "Abierto"),
            new Pasillo("Pasillo Z", "Salida Norte", 5, "Bloqueado"),
        };
        Dijkstra d = new Dijkstra(new Grafo(p), Dijkstra.SALIDAS);
        assertEquals(0, d.ruta("Sala X").length);
        assertNull(d.salida("Sala X"));
    }

    @Test
    void rf01InvalidoSalaInexistente() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> guia.ruta("XYZ-999"));
        assertEquals("No existe el lugar XYZ-999", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> guia.salida("XYZ-999"));
    }

    @Test
    void rf01InvalidoLugarNulo() {
        assertThrows(IllegalArgumentException.class, () -> guia.ruta(null));
    }

    @Test
    void rf01InvalidoPasilloConTiempoNegativo() {
        Pasillo[] p = { new Pasillo("A", "B", -4, "Abierto") };
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Grafo(p));
        assertEquals("Tiempo inválido en el pasillo A - B: -4", e.getMessage());
    }

    @Test
    void rf01InvalidoListaDePasillosNula() {
        assertThrows(IllegalArgumentException.class, () -> new Grafo(null));
    }

    // ===== RF-02: indicar los segundos estimados =====

    @Test
    void rf02ValorExactoSegundosDeLasSeisSalas() {
        assertEquals(30, guia.segundos("LAB-101"));
        assertEquals(35, guia.segundos("LAB-102"));
        assertEquals(37, guia.segundos("RM-103"));
        assertEquals(27, guia.segundos("RM-104"));
        assertEquals(25, guia.segundos("OFF-105"));
        assertEquals(25, guia.segundos("LIB-001"));
    }

    @Test
    void rf02ValorExactoLosSegundosSonLaSumaDeLosPasillosDeLaRuta() {
        for (String sala : SEIS_SALAS) {
            String[] r = guia.ruta(sala);
            int suma = 0;
            for (int i = 1; i < r.length; i++) suma += grafo.peso(r[i - 1], r[i]);
            assertEquals(guia.segundos(sala), suma, sala);
        }
    }

    @Test
    void rf02LimiteOrigenIgualAlDestinoEsCero() {
        assertEquals(0, guia.segundos("Salida Sur"));
    }

    @Test
    void rf02LimiteSinRutaDevuelveMenosUno() {
        Pasillo[] p = { new Pasillo("Sala X", "Salida Norte", 5, "Cerrado") };
        assertEquals(-1, new Dijkstra(new Grafo(p), Dijkstra.SALIDAS).segundos("Sala X"));
    }

    @Test
    void rf02InvalidoSalaInexistente() {
        assertThrows(IllegalArgumentException.class, () -> guia.segundos("XYZ-999"));
    }

    // ===== RNF-01: tienen que salir lo más rápido posible =====

    @Test
    void rnf01EligeLaRutaMasRapidaNoLaDeMenosPasillos() {
        // RM-103 → Pasillo B → Pasillo C → Salida Sur = 40 s, pero RM-103 → RM-104 → Pasillo C → Salida Sur = 37 s.
        assertEquals(37, guia.segundos("RM-103"));
        assertEquals("RM-104", guia.ruta("RM-103")[1]);
    }

    @Test
    void rnf01PasillosSeRecorrenEnAmbosSentidos() {
        assertEquals(6, grafo.peso("LAB-101", "LAB-102"));
        assertEquals(6, grafo.peso("LAB-102", "LAB-101"));
    }

    // ===== RNF-02: descartar pasillos cerrados o bloqueados =====

    @Test
    void rnf02ValorExactoPasilloBloqueadoNoExisteEnElGrafo() {
        assertEquals(Grafo.SIN_ARISTA, grafo.peso("Pasillo B", "Salida Este"));
        assertEquals(Grafo.SIN_ARISTA, grafo.peso("Salida Este", "Pasillo B"));
    }

    @Test
    void rnf02NingunaRutaUsaElPasilloBloqueado() {
        for (String s : SEIS_SALAS) {
            String[] r = guia.ruta(s);
            for (int i = 1; i < r.length; i++) {
                boolean usaBloqueado = (r[i - 1].equals("Pasillo B") && r[i].equals("Salida Este"))
                    || (r[i - 1].equals("Salida Este") && r[i].equals("Pasillo B"));
                assertFalse(usaBloqueado, s);
            }
        }
    }

    @Test
    void rnf02SiElPasilloEstuvieraAbiertoSeUsaria() {
        Pasillo[] abiertos = {
            new Pasillo("RM-103", "Pasillo B", 8, "Abierto"),
            new Pasillo("Pasillo B", "Salida Este", 20, "Abierto"),
        };
        assertEquals(28, new Dijkstra(new Grafo(abiertos), Dijkstra.SALIDAS).segundos("RM-103"));
    }

    @Test
    void rnf02PasilloCerradoTambienSeDescarta() {
        Pasillo[] p = {
            new Pasillo("Sala X", "Salida Norte", 5, "Cerrado"),
            new Pasillo("Sala X", "Salida Sur", 50, "Abierto"),
        };
        Dijkstra d = new Dijkstra(new Grafo(p), Dijkstra.SALIDAS);
        assertEquals(50, d.segundos("Sala X"));
        assertEquals("Salida Sur", d.salida("Sala X"));
    }

    // ===== RNF-03: solo puede usar alguna de las 3 salidas disponibles =====

    @Test
    void rnf03LasSeisSalasTerminanEnUnaDeLasTresSalidas() {
        for (String s : SEIS_SALAS) {
            String fin = guia.salida(s);
            assertTrue(fin.equals("Salida Norte") || fin.equals("Salida Sur") || fin.equals("Salida Este"), s);
        }
    }

    @Test
    void rnf03UnLugarLlamadoSalidaQueNoEsDeLasTresNoSeUsa() {
        Pasillo[] p = {
            new Pasillo("Sala X", "Salida Oeste", 1, "Abierto"),
            new Pasillo("Sala X", "Salida Norte", 30, "Abierto"),
        };
        Dijkstra d = new Dijkstra(new Grafo(p), Dijkstra.SALIDAS);
        assertEquals(30, d.segundos("Sala X"));
        assertEquals("Salida Norte", d.salida("Sala X"));
    }
}
