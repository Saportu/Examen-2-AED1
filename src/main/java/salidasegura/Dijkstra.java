package salidasegura;

/**
 * F3 — Guía de evacuación: Dijkstra con arreglos, O(V²).
 * Se ejecuta una vez desde las 3 salidas a la vez (todas con distancia 0); como los
 * pasillos son de doble sentido, dist[v] es el menor tiempo de v a alguna salida y
 * siguiente[v] es el próximo lugar a caminar desde v hacia esa salida.
 */
public class Dijkstra {

    public static final String[] SALIDAS = { "Salida Norte", "Salida Sur", "Salida Este" }; // RNF-03

    private static final int INFINITO = Integer.MAX_VALUE;

    private final Grafo grafo;
    private final int[] dist;
    private final int[] siguiente;

    public Dijkstra(Grafo grafo, String[] salidas) {
        this.grafo = grafo;
        int n = grafo.cantidad();
        dist = new int[n];
        siguiente = new int[n];
        boolean[] visitado = new boolean[n];
        for (int i = 0; i < n; i++) {
            dist[i] = INFINITO;
            siguiente[i] = -1;
        }
        for (String salida : salidas) {
            int s = grafo.indice(salida);
            if (s >= 0) dist[s] = 0;
        }

        for (int paso = 0; paso < n; paso++) {
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] != INFINITO && (u == -1 || dist[i] < dist[u])) u = i;
            }
            if (u == -1) break;
            visitado[u] = true;
            for (int v = 0; v < n; v++) {
                int w = grafo.peso(u, v);
                if (w == Grafo.SIN_ARISTA || visitado[v]) continue;
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    siguiente[v] = u;
                }
            }
        }
    }

    /** RF-02: segundos estimados hasta la salida, o -1 si no hay ruta. */
    public int segundos(String lugar) {
        int i = grafo.indice(lugar);
        return (i < 0 || dist[i] == INFINITO) ? -1 : dist[i];
    }

    /** RF-01: lugares a recorrer desde la sala hasta la salida (vacío si no hay ruta). */
    public String[] ruta(String lugar) {
        if (segundos(lugar) < 0) return new String[0];
        int largo = 0;
        for (int v = grafo.indice(lugar); v != -1; v = siguiente[v]) largo++;
        String[] ruta = new String[largo];
        int k = 0;
        for (int v = grafo.indice(lugar); v != -1; v = siguiente[v]) ruta[k++] = grafo.nombre(v);
        return ruta;
    }

    /** RF-01: salida que debe usar la sala, o null si no hay ruta. */
    public String salida(String lugar) {
        String[] r = ruta(lugar);
        return r.length == 0 ? null : r[r.length - 1];
    }
}
