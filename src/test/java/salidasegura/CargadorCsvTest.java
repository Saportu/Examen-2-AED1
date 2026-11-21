package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CargadorCsvTest {

    @Test
    void leeLasSeisSalas() {
        Sala[] salas = CargadorCsv.salas("/salas.csv");
        assertEquals(6, salas.length);
        assertEquals("LAB-101", salas[0].codigo);
        assertEquals("Laboratorio de química", salas[0].nombre);
        assertEquals("Oeste", salas[0].ala);
        assertEquals(24, salas[0].ocupantes);
        assertEquals("LIB-001", salas[5].codigo);
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
            }
        }
        assertEquals(1, bloqueados);
    }
}
