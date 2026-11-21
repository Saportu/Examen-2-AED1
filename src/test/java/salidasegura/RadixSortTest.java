package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** F2 — Reporte para la brigada (radix sort). */
class RadixSortTest {

    private static void verificarCodigos(Sala[] reporte, String... codigos) {
        assertEquals(codigos.length, reporte.length);
        for (int i = 0; i < codigos.length; i++) assertEquals(codigos[i], reporte[i].codigo, "posición " + i);
    }

    // ===== RF-01: se activa la alarma =====

    @Test
    void rf01ValorExactoAlActivarLaAlarmaSeGeneraElReporte() {
        ArbolEdificio arbol = new ArbolEdificio(CargadorCsv.salas("/salas.csv"));
        Sala[] reporte = arbol.activarAlarma();
        assertEquals(6, reporte.length);
        assertEquals("LIB-001", reporte[0].codigo);
        assertEquals(45, reporte[0].ocupantes);
    }

    @Test
    void rf01LimiteAlarmaEnEdificioSinSalas() {
        assertEquals(0, new ArbolEdificio(new Sala[0]).activarAlarma().length);
    }

    @Test
    void rf01LimiteAlarmaUsaLosOcupantesActualizados() {
        ArbolEdificio arbol = new ArbolEdificio(CargadorCsv.salas("/salas.csv"));
        arbol.actualizarOcupantes("OFF-105", 80);
        Sala[] reporte = arbol.activarAlarma();
        assertEquals("OFF-105", reporte[0].codigo);
        assertEquals(80, reporte[0].ocupantes);
    }

    // ===== RF-02: lista las salas de más a menos ocupantes =====

    @Test
    void rf02ValorExactoOrdenCompleto() {
        Sala[] reporte = new ArbolEdificio(CargadorCsv.salas("/salas.csv")).activarAlarma();
        verificarCodigos(reporte, "LIB-001", "RM-103", "LAB-102", "RM-104", "LAB-101", "OFF-105");
        int[] ocupantes = { 45, 45, 30, 30, 24, 6 };
        for (int i = 0; i < ocupantes.length; i++) assertEquals(ocupantes[i], reporte[i].ocupantes, "posición " + i);
    }

    @Test
    void rf02LimiteUnaSola() {
        verificarCodigos(RadixSort.ordenarReporte(new Sala[] { new Sala("A", "", "X", 7) }), "A");
    }

    @Test
    void rf02LimiteArregloVacio() {
        assertEquals(0, RadixSort.ordenarReporte(new Sala[0]).length);
    }

    @Test
    void rf02LimiteValoresExtremosCeroYOchenta() {
        Sala[] salas = { new Sala("A", "", "X", 0), new Sala("B", "", "X", 80), new Sala("C", "", "X", 9), new Sala("D", "", "X", 10) };
        verificarCodigos(RadixSort.ordenarReporte(salas), "B", "D", "C", "A");
    }

    @Test
    void rf02LimiteYaOrdenadoEInverso() {
        Sala[] desc = { new Sala("A", "", "X", 3), new Sala("B", "", "X", 2), new Sala("C", "", "X", 1) };
        Sala[] asc = { new Sala("C", "", "X", 1), new Sala("B", "", "X", 2), new Sala("A", "", "X", 3) };
        verificarCodigos(RadixSort.ordenarReporte(desc), "A", "B", "C");
        verificarCodigos(RadixSort.ordenarReporte(asc), "A", "B", "C");
    }

    @Test
    void rf02NoModificaElArregloOriginal() {
        Sala[] salas = { new Sala("B", "", "A", 1), new Sala("A", "", "A", 2) };
        RadixSort.ordenarReporte(salas);
        assertEquals("B", salas[0].codigo);
        assertEquals("A", salas[1].codigo);
    }

    @Test
    void rf02InvalidoArregloNulo() {
        assertThrows(IllegalArgumentException.class, () -> RadixSort.ordenarReporte(null));
    }

    @Test
    void rf02InvalidoSalaNula() {
        assertThrows(IllegalArgumentException.class,
            () -> RadixSort.ordenarReporte(new Sala[] { new Sala("A", "", "X", 1), null }));
    }

    @Test
    void rf02InvalidoOcupantesNegativos() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> RadixSort.ordenarReporte(new Sala[] { new Sala("A", "", "X", -3) }));
        assertEquals("La sala A tiene ocupantes negativos: -3", e.getMessage());
    }

    // ===== RNF-01: en caso de empate, se listan por sala de A-Z =====

    @Test
    void rnf01EmpateDeLosDatosReales() {
        Sala[] reporte = new ArbolEdificio(CargadorCsv.salas("/salas.csv")).activarAlarma();
        assertEquals("LIB-001", reporte[0].codigo); // 45, antes que RM-103
        assertEquals("RM-103", reporte[1].codigo);
        assertEquals("LAB-102", reporte[2].codigo); // 30, antes que RM-104
        assertEquals("RM-104", reporte[3].codigo);
    }

    @Test
    void rnf01LimiteTodasEmpatadasYCodigosDeDistintoLargo() {
        Sala[] salas = {
            new Sala("RM-2", "", "A", 10),
            new Sala("AB-10", "", "A", 10),
            new Sala("AB-1", "", "A", 10),
            new Sala("ZZ-9", "", "A", 11),
        };
        verificarCodigos(RadixSort.ordenarReporte(salas), "ZZ-9", "AB-1", "AB-10", "RM-2");
    }

    // ===== RNF-02: debe durar menos de 1s (400 salas) =====

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
        assertEquals(80, r[0].ocupantes);
        assertEquals(0, r[399].ocupantes);
        for (int i = 1; i < r.length; i++) {
            boolean ok = r[i - 1].ocupantes > r[i].ocupantes
                || (r[i - 1].ocupantes == r[i].ocupantes && r[i - 1].codigo.compareTo(r[i].codigo) <= 0);
            assertTrue(ok, "desorden en posición " + i);
        }
    }
}
