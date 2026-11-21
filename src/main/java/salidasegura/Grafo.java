package salidasegura;

/**
 * Grafo no dirigido (pasillos en ambos sentidos) con matriz de adyacencia.
 * Los pasillos bloqueados no se agregan (F3 RNF-02).
 */
public class Grafo {

    public static final int SIN_ARISTA = -1;

    private String[] nombres = new String[16];
    private int cantidad;
    private int[][] pesos = new int[0][0];

    public Grafo(Pasillo[] pasillos) {
        if (pasillos == null) throw new IllegalArgumentException("El arreglo de pasillos es nulo");
        for (Pasillo p : pasillos) {
            if (p.segundos < 0) {
                throw new IllegalArgumentException(
                    "Tiempo inválido en el pasillo " + p.desde + " - " + p.hacia + ": " + p.segundos);
            }
            indiceOAgregar(p.desde);
            indiceOAgregar(p.hacia);
        }
        pesos = new int[cantidad][cantidad];
        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) pesos[i][j] = SIN_ARISTA;
        }
        for (Pasillo p : pasillos) {
            if (p.bloqueado()) continue;
            int a = indice(p.desde), b = indice(p.hacia);
            pesos[a][b] = p.segundos;
            pesos[b][a] = p.segundos;
        }
    }

    public int cantidad() {
        return cantidad;
    }

    public String nombre(int i) {
        return nombres[i];
    }

    /** Índice del lugar o -1 si no existe. */
    public int indice(String nombre) {
        for (int i = 0; i < cantidad; i++) if (nombres[i].equals(nombre)) return i;
        return -1;
    }

    public int peso(int a, int b) {
        return pesos[a][b];
    }

    public int peso(String a, String b) {
        int i = indice(a), j = indice(b);
        return (i < 0 || j < 0) ? SIN_ARISTA : pesos[i][j];
    }

    private void indiceOAgregar(String nombre) {
        if (indice(nombre) >= 0) return;
        if (cantidad == nombres.length) {
            String[] nuevos = new String[nombres.length * 2];
            for (int i = 0; i < cantidad; i++) nuevos[i] = nombres[i];
            nombres = nuevos;
        }
        nombres[cantidad++] = nombre;
    }
}
