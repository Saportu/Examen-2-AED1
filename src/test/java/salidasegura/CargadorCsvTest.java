package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Carga de datos (salas y pasillos) desde CSV. */
class CargadorCsvTest {

    // ---- Valor exacto ----

    @Test
    void leeLasSeisSalasConSusValoresExactos() {
        Sala[] salas = CargadorCsv.salas("/salas.csv");
        assertEquals(6, salas.length);
        assertEquals("LAB-101", salas[0].codigo);
        assertEquals("Laboratorio de química", salas[0].nombre);
        assertEquals("Oeste", salas[0].ala);
        assertEquals(24, salas[0].ocupantes);
        assertEquals("LIB-001", salas[5].codigo);
        assertEquals("Biblioteca", salas[5].nombre);
        assertEquals("Este", salas[5].ala);
        assertEquals(45, salas[5].ocupantes);
    }

    @Test
    void leeLosQuincePasillosConUnoBloqueado() {
        Pasillo[] pasillos = CargadorCsv.pasillos("/pasillos.csv");
        assertEquals(15, pasillos.length);
        int bloqueados = 0;
        for (Pasillo p : pasillos) {
            if (p.bloqueado()) {
                bloqueados++;
                assertEquals("Pasillo B", p.desde);
                assertEquals("Salida Este", p.hacia);
                assertEquals(20, p.segundos);
                assertEquals("Bloqueado (humo)", p.estado);
            }
        }
        assertEquals(1, bloqueados);
    }

    // ---- Casos límite ----

    @Test
    void archivoSoloConEncabezadoDaArregloVacio() {
        assertEquals(0, CargadorCsv.salas("/salas_vacio.csv").length);
    }

    // ---- Entradas inválidas / datos mal formados ----

    @Test
    void lineaConColumnasFaltantesLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> CargadorCsv.salas("/salas_faltan_columnas.csv"));
    }

    @Test
    void ocupantesNoNumericoLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> CargadorCsv.salas("/salas_ocupantes_no_numerico.csv"));
    }

    @Test
    void segundosNoNumericoLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> CargadorCsv.pasillos("/pasillos_segundos_no_numerico.csv"));
    }

    @Test
    void archivoInexistenteLanzaError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> CargadorCsv.salas("/no_existe.csv"));
        assertEquals("No existe el recurso /no_existe.csv", e.getMessage());
    }
}
