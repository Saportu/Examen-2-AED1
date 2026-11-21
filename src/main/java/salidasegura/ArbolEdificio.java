package salidasegura;

/**
 * F1 — Directorio del edificio: árbol n-ario Edificio → Alas → Salas.
 * Actualizar una sala propaga la diferencia hacia arriba en O(h);
 * consultar el total de un ala o del edificio es leer un nodo.
 */
public class ArbolEdificio {

    public static final int MAX_OCUPANTES = 80; // RNF-01

    private final Nodo raiz = new Nodo("Edificio", null, null);

    public ArbolEdificio(Sala[] salas) {
        this(new String[0], salas);
    }

    /** Registra primero las alas dadas (pueden quedar vacías) y luego las salas. */
    public ArbolEdificio(String[] alas, Sala[] salas) {
        for (String nombre : alas) {
            if (raiz.buscarHijo(nombre) == null) raiz.agregarHijo(new Nodo(nombre, raiz, null));
        }
        for (Sala s : salas) {
            validar(s.ocupantes, s.codigo);
            Nodo ala = raiz.buscarHijo(s.ala);
            if (ala == null) ala = raiz.agregarHijo(new Nodo(s.ala, raiz, null));
            Nodo hoja = ala.agregarHijo(new Nodo(s.codigo, ala, s));
            propagar(hoja, s.ocupantes);
        }
    }

    /** RF-01: salas de un ala. */
    public Sala[] listarSalas(String ala) {
        Nodo nodoAla = ala(ala);
        Sala[] salas = new Sala[nodoAla.cantidadHijos];
        for (int i = 0; i < salas.length; i++) salas[i] = nodoAla.hijos[i].sala;
        return salas;
    }

    /** RF-02: actualiza los ocupantes de una sala (RNF-01: máximo 80). */
    public void actualizarOcupantes(String codigo, int ocupantes) {
        validar(ocupantes, codigo);
        Nodo hoja = buscarSala(codigo);
        if (hoja == null) throw new IllegalArgumentException("No existe la sala " + codigo);
        int delta = ocupantes - hoja.sala.ocupantes;
        hoja.sala.ocupantes = ocupantes;
        propagar(hoja, delta);
    }

    /** RF-03: personas en un ala. */
    public int ocupantes(String ala) {
        return ala(ala).total;
    }

    /** RF-03: personas en todo el edificio. */
    public int ocupantesEdificio() {
        return raiz.total;
    }

    /** Nombres de las alas en orden de carga. */
    public String[] alas() {
        String[] nombres = new String[raiz.cantidadHijos];
        for (int i = 0; i < nombres.length; i++) nombres[i] = raiz.hijos[i].nombre;
        return nombres;
    }

    /** F2 RF-01: al activarse la alarma se genera el reporte para la brigada. */
    public Sala[] activarAlarma() {
        Sala[] todas = new Sala[contarSalas()];
        int k = 0;
        for (int a = 0; a < raiz.cantidadHijos; a++) {
            Nodo ala = raiz.hijos[a];
            for (int s = 0; s < ala.cantidadHijos; s++) todas[k++] = ala.hijos[s].sala;
        }
        return RadixSort.ordenarReporte(todas);
    }

    private int contarSalas() {
        int n = 0;
        for (int a = 0; a < raiz.cantidadHijos; a++) n += raiz.hijos[a].cantidadHijos;
        return n;
    }

    private Nodo ala(String ala) {
        Nodo nodo = raiz.buscarHijo(ala);
        if (nodo == null) throw new IllegalArgumentException("No existe el ala " + ala);
        return nodo;
    }

    private Nodo buscarSala(String codigo) {
        for (int a = 0; a < raiz.cantidadHijos; a++) {
            Nodo hoja = raiz.hijos[a].buscarHijo(codigo);
            if (hoja != null) return hoja;
        }
        return null;
    }

    /** Sube desde la hoja hasta la raíz sumando delta: O(h). */
    private static void propagar(Nodo nodo, int delta) {
        for (Nodo n = nodo; n != null; n = n.padre) n.total += delta;
    }

    private static void validar(int ocupantes, String codigo) {
        if (ocupantes < 0 || ocupantes > MAX_OCUPANTES) {
            throw new IllegalArgumentException(
                "La sala " + codigo + " debe tener entre 0 y " + MAX_OCUPANTES + " personas (recibido " + ocupantes + ")");
        }
    }
}
