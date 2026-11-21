package salidasegura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** F1 — Directorio del edificio (árbol n-ario). */
class ArbolEdificioTest {

    private ArbolEdificio arbol;

    @BeforeEach
    void cargar() {
        arbol = new ArbolEdificio(CargadorCsv.salas("/salas.csv"));
    }

    @Test
    void rf01ListaLasSalasDeUnAla() {
        Sala[] este = arbol.listarSalas("Este");
        assertEquals(2, este.length);
        assertEquals("OFF-105", este[0].codigo);
        assertEquals("LIB-001", este[1].codigo);
    }

    @Test
    void rf01AlaInexistenteLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> arbol.listarSalas("Norte"));
    }

    @Test
    void rf03PersonasPorAlaYEdificio() {
        assertEquals(54, arbol.ocupantes("Oeste"));
        assertEquals(75, arbol.ocupantes("Central"));
        assertEquals(51, arbol.ocupantes("Este"));
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rf02ActualizarPropagaAlAlaYAlEdificio() {
        arbol.actualizarOcupantes("RM-103", 50);
        assertEquals(50, arbol.listarSalas("Central")[0].ocupantes);
        assertEquals(80, arbol.ocupantes("Central"));
        assertEquals(185, arbol.ocupantesEdificio());
        assertEquals(54, arbol.ocupantes("Oeste"));
    }

    @Test
    void rf02SalaInexistenteLanzaError() {
        assertThrows(IllegalArgumentException.class, () -> arbol.actualizarOcupantes("XYZ-999", 5));
    }

    @Test
    void rnf01NingunaSalaPuedeTenerMasDe80() {
        assertThrows(IllegalArgumentException.class, () -> arbol.actualizarOcupantes("RM-103", 81));
        assertEquals(45, arbol.listarSalas("Central")[0].ocupantes);
        assertEquals(75, arbol.ocupantes("Central"));
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rnf01ExactamenteOchentaSePermite() {
        arbol.actualizarOcupantes("OFF-105", 80);
        assertEquals(125, arbol.ocupantes("Este"));
    }

    @Test
    void rnf01CargaInicialConMasDe80Falla() {
        Sala[] salas = { new Sala("X-1", "X", "Oeste", 81) };
        assertThrows(IllegalArgumentException.class, () -> new ArbolEdificio(salas));
    }
}
