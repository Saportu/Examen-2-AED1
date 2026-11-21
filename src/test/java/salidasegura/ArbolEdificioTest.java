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

    // ===== RF-01: Seguridad debe poder listar las salas de un ala =====

    @Test
    void rf01ValorExactoSalasDeCadaAla() {
        Sala[] oeste = arbol.listarSalas("Oeste");
        assertEquals(2, oeste.length);
        assertEquals("LAB-101", oeste[0].codigo);
        assertEquals("LAB-102", oeste[1].codigo);

        Sala[] central = arbol.listarSalas("Central");
        assertEquals("RM-103", central[0].codigo);
        assertEquals("RM-104", central[1].codigo);

        Sala[] este = arbol.listarSalas("Este");
        assertEquals(2, este.length);
        assertEquals("OFF-105", este[0].codigo);
        assertEquals("LIB-001", este[1].codigo);
    }

    @Test
    void rf01LimiteAlaVaciaDevuelveArregloVacio() {
        ArbolEdificio conAlaVacia = new ArbolEdificio(
            new String[] { "Oeste", "Norte" }, new Sala[] { new Sala("LAB-101", "Lab", "Oeste", 10) });
        assertEquals(0, conAlaVacia.listarSalas("Norte").length);
    }

    @Test
    void rf01LimiteEdificioSinSalas() {
        ArbolEdificio vacio = new ArbolEdificio(new Sala[0]);
        assertEquals(0, vacio.alas().length);
        assertThrows(IllegalArgumentException.class, () -> vacio.listarSalas("Oeste"));
    }

    @Test
    void rf01InvalidoAlaInexistente() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> arbol.listarSalas("Sur"));
        assertEquals("No existe el ala Sur", e.getMessage());
    }

    @Test
    void rf01InvalidoAlaNula() {
        assertThrows(IllegalArgumentException.class, () -> arbol.listarSalas(null));
    }

    // ===== RF-02: Actualizar ocupantes de una sala =====

    @Test
    void rf02ValorExactoActualizaSalaAlaYEdificio() {
        arbol.actualizarOcupantes("RM-103", 50);
        assertEquals(50, arbol.listarSalas("Central")[0].ocupantes);
        assertEquals(80, arbol.ocupantes("Central"));
        assertEquals(185, arbol.ocupantesEdificio());
        assertEquals(54, arbol.ocupantes("Oeste"));
        assertEquals(51, arbol.ocupantes("Este"));
    }

    @Test
    void rf02ValorExactoDisminuir() {
        arbol.actualizarOcupantes("LIB-001", 5);
        assertEquals(11, arbol.ocupantes("Este"));
        assertEquals(140, arbol.ocupantesEdificio());
    }

    @Test
    void rf02LimiteMismoValorNoCambiaTotales() {
        arbol.actualizarOcupantes("LAB-101", 24);
        assertEquals(54, arbol.ocupantes("Oeste"));
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rf02LimiteCeroOcupantes() {
        arbol.actualizarOcupantes("OFF-105", 0);
        assertEquals(0, arbol.listarSalas("Este")[0].ocupantes);
        assertEquals(45, arbol.ocupantes("Este"));
        assertEquals(174, arbol.ocupantesEdificio());
    }

    @Test
    void rf02LimiteVariasActualizacionesSeguidas() {
        arbol.actualizarOcupantes("RM-104", 10);
        arbol.actualizarOcupantes("RM-104", 70);
        arbol.actualizarOcupantes("RM-103", 0);
        assertEquals(70, arbol.ocupantes("Central"));
        assertEquals(175, arbol.ocupantesEdificio());
    }

    @Test
    void rf02InvalidoSalaInexistente() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> arbol.actualizarOcupantes("XYZ-999", 5));
        assertEquals("No existe la sala XYZ-999", e.getMessage());
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rf02InvalidoCodigoNulo() {
        assertThrows(IllegalArgumentException.class, () -> arbol.actualizarOcupantes(null, 5));
    }

    @Test
    void rf02InvalidoOcupantesNegativos() {
        assertThrows(IllegalArgumentException.class, () -> arbol.actualizarOcupantes("LAB-101", -1));
        assertEquals(24, arbol.listarSalas("Oeste")[0].ocupantes);
        assertEquals(180, arbol.ocupantesEdificio());
    }

    // ===== RF-03: Obtener cantidad de personas en cualquier ala o en el edificio =====

    @Test
    void rf03ValorExactoPorAlaYEdificio() {
        assertEquals(54, arbol.ocupantes("Oeste"));
        assertEquals(75, arbol.ocupantes("Central"));
        assertEquals(51, arbol.ocupantes("Este"));
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rf03LimiteAlaVaciaYEdificioVacio() {
        ArbolEdificio conAlaVacia = new ArbolEdificio(new String[] { "Norte" }, new Sala[0]);
        assertEquals(0, conAlaVacia.ocupantes("Norte"));
        assertEquals(0, conAlaVacia.ocupantesEdificio());
        assertEquals(0, new ArbolEdificio(new Sala[0]).ocupantesEdificio());
    }

    @Test
    void rf03LimiteAlaConTodasLasSalasEnCero() {
        arbol.actualizarOcupantes("LAB-101", 0);
        arbol.actualizarOcupantes("LAB-102", 0);
        assertEquals(0, arbol.ocupantes("Oeste"));
        assertEquals(126, arbol.ocupantesEdificio());
    }

    @Test
    void rf03InvalidoAlaInexistente() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> arbol.ocupantes("Sur"));
        assertEquals("No existe el ala Sur", e.getMessage());
    }

    // ===== RNF-01: Ninguna sala puede tener más de 80 personas =====

    @Test
    void rnf01LimiteExactamenteOchentaSePermite() {
        arbol.actualizarOcupantes("OFF-105", 80);
        assertEquals(80, arbol.listarSalas("Este")[0].ocupantes);
        assertEquals(125, arbol.ocupantes("Este"));
        assertEquals(254, arbol.ocupantesEdificio());
    }

    @Test
    void rnf01OchentaYUnoSeRechazaYNadaCambia() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
            () -> arbol.actualizarOcupantes("RM-103", 81));
        assertEquals("La sala RM-103 debe tener entre 0 y 80 personas (recibido 81)", e.getMessage());
        assertEquals(45, arbol.listarSalas("Central")[0].ocupantes);
        assertEquals(75, arbol.ocupantes("Central"));
        assertEquals(180, arbol.ocupantesEdificio());
    }

    @Test
    void rnf01CargaInicialConMasDe80Falla() {
        Sala[] salas = { new Sala("X-1", "X", "Oeste", 81) };
        assertThrows(IllegalArgumentException.class, () -> new ArbolEdificio(salas));
    }
}
