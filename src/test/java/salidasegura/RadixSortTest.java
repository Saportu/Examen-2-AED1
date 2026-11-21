package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** F2 — Reporte para la brigada (radix sort). */
class RadixSortTest {

    @Test
    void rf01Rf02AlarmaListaDeMasAMenosOcupantes() {
        ArbolEdificio arbol = new ArbolEdificio(CargadorCsv.salas("/salas.csv"));
        Sala[] reporte = arbol.activarAlarma();

        String[] codigos = { "LIB-001", "RM-103", "LAB-102", "RM-104", "LAB-101", "OFF-105" };
        int[] ocupantes = { 45, 45, 30, 30, 24, 6 };
        assertEquals(6, reporte.length);
        for (int i = 0; i < codigos.length; i++) {
            assertEquals(codigos[i], reporte[i].codigo, "posición " + i);
            assertEquals(ocupantes[i], reporte[i].ocupantes, "posición " + i);
        }
    }

    @Test
    void rnf01EmpateSeOrdenaDeAaZ() {
        Sala[] salas = {
            new Sala("RM-2", "", "A", 10),
            new Sala("AB-10", "", "A", 10),
            new Sala("AB-1", "", "A", 10),
            new Sala("ZZ-9", "", "A", 11),
        };
        Sala[] r = RadixSort.ordenarReporte(salas);
        assertEquals("ZZ-9", r[0].codigo);
        assertEquals("AB-1", r[1].codigo);
        assertEquals("AB-10", r[2].codigo);
        assertEquals("RM-2", r[3].codigo);
    }

    @Test
    void noModificaElArregloOriginal() {
        Sala[] salas = { new Sala("B", "", "A", 1), new Sala("A", "", "A", 2) };
        RadixSort.ordenarReporte(salas);
        assertEquals("B", salas[0].codigo);
    }

    @Test
    void rnf02CuatrocientasSalasEnMenosDeUnSegundo() {
        Sala[] salas = new Sala[400];
        for (int i = 0; i < 400; i++) {
            String codigo = "S-" + String.format("%03d", (i * 7919) % 400);
            salas[i] = new Sala(codigo, "Sala " + i, "Ala" + (i % 4), (i * 37) % 81);
        }
        long inicio = System.nanoTime();
        Sala[] r = RadixSort.ordenarReporte(salas);
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        assertTrue(ms < 1000, "tardó " + ms + " ms");
        assertEquals(400, r.length);
        for (int i = 1; i < r.length; i++) {
            boolean ok = r[i - 1].ocupantes > r[i].ocupantes
                || (r[i - 1].ocupantes == r[i].ocupantes && r[i - 1].codigo.compareTo(r[i].codigo) <= 0);
            assertTrue(ok, "desorden en posición " + i);
        }
    }
}
