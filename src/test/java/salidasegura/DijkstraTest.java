package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** F3 — Guía de evacuación (Dijkstra O(V²), pasillos en ambos sentidos). */
class DijkstraTest {

    private Grafo grafo;
    private Dijkstra guia;

    @BeforeEach
    void cargar() {
        grafo = new Grafo(CargadorCsv.pasillos("/pasillos.csv"));
        guia = new Dijkstra(grafo, Dijkstra.SALIDAS);
    }

    private void verificar(String sala, int segundos, String... ruta) {
        assertEquals(segundos, guia.segundos(sala), sala);
        assertArrayEquals(ruta, guia.ruta(sala), sala);
    }

    @Test
    void rf01Rf02RutasYSegundosDeLasSeisSalas() {
        verificar("LAB-101", 30, "LAB-101", "Pasillo A", "Salida Norte");
        verificar("LAB-102", 35, "LAB-102", "Pasillo A", "Salida Norte");
        verificar("RM-103", 37, "RM-103", "RM-104", "Pasillo C", "Salida Sur");
        verificar("RM-104", 27, "RM-104", "Pasillo C", "Salida Sur");
        verificar("OFF-105", 25, "OFF-105", "Pasillo C", "Salida Sur");
        verificar("LIB-001", 25, "LIB-001", "Salida Este");
    }

    @Test
    void rf01SalidaQueUsaCadaSala() {
        assertEquals("Salida Norte", guia.salida("LAB-101"));
        assertEquals("Salida Sur", guia.salida("RM-103"));
        assertEquals("Salida Este", guia.salida("LIB-001"));
    }

    @Test
    void pasillosSeRecorrenEnAmbosSentidos() {
        assertEquals(6, grafo.peso("LAB-101", "LAB-102"));
        assertEquals(6, grafo.peso("LAB-102", "LAB-101"));
    }

    @Test
    void rnf02DescartaPasillosBloqueados() {
        assertEquals(Grafo.SIN_ARISTA, grafo.peso("Pasillo B", "Salida Este"));
        String[] salas = { "LAB-101", "LAB-102", "RM-103", "RM-104", "OFF-105", "LIB-001" };
        for (String s : salas) {
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
        // Mismo grafo sin bloqueo: RM-103 → Pasillo B → Salida Este = 28 s (< 37 s).
        Pasillo[] abiertos = {
            new Pasillo("RM-103", "Pasillo B", 8, "Abierto"),
            new Pasillo("Pasillo B", "Salida Este", 20, "Abierto"),
        };
        Dijkstra d = new Dijkstra(new Grafo(abiertos), new String[] { "Salida Este" });
        assertEquals(28, d.segundos("RM-103"));
    }

    @Test
    void rnf03SoloTerminaEnUnaDeLasTresSalidas() {
        String[] salas = { "LAB-101", "LAB-102", "RM-103", "RM-104", "OFF-105", "LIB-001" };
        for (String s : salas) {
            String fin = guia.salida(s);
            assertTrue(fin.equals("Salida Norte") || fin.equals("Salida Sur") || fin.equals("Salida Este"), s);
        }
    }

    @Test
    void sinRutaDisponibleLoIndica() {
        Pasillo[] p = {
            new Pasillo("Sala X", "Pasillo Z", 5, "Abierto"),
            new Pasillo("Pasillo Z", "Salida Norte", 5, "Bloqueado"),
        };
        Dijkstra d = new Dijkstra(new Grafo(p), new String[] { "Salida Norte" });
        assertEquals(-1, d.segundos("Sala X"));
        assertEquals(0, d.ruta("Sala X").length);
    }
}
