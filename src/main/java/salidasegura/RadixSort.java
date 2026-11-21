package salidasegura;

/**
 * F2 — Reporte para la brigada: radix sort LSD (estable), O(n).
 * Primero ordena por código A–Z (desempate, RNF-01) y luego, de forma estable,
 * por ocupantes de mayor a menor (RF-02).
 */
public final class RadixSort {

    private static final int ALFABETO = 65536; // todos los valores de char

    private RadixSort() {}

    /** Devuelve una copia ordenada; no modifica el arreglo recibido. */
    public static Sala[] ordenarReporte(Sala[] salas) {
        if (salas == null) throw new IllegalArgumentException("El arreglo de salas es nulo");
        Sala[] a = new Sala[salas.length];
        for (int i = 0; i < salas.length; i++) {
            Sala s = salas[i];
            if (s == null) throw new IllegalArgumentException("La sala en la posición " + i + " es nula");
            if (s.ocupantes < 0) {
                throw new IllegalArgumentException("La sala " + s.codigo + " tiene ocupantes negativos: " + s.ocupantes);
            }
            a[i] = s;
        }
        a = ordenarPorCodigo(a);
        return ordenarPorOcupantesDesc(a);
    }

    /** LSD por caracteres; los códigos cortos se rellenan con 0 (van antes: "AB-1" < "AB-10"). */
    private static Sala[] ordenarPorCodigo(Sala[] a) {
        int largo = 0;
        for (Sala s : a) if (s.codigo.length() > largo) largo = s.codigo.length();
        for (int pos = largo - 1; pos >= 0; pos--) {
            int[] conteo = new int[ALFABETO + 1];
            for (Sala s : a) conteo[caracter(s.codigo, pos) + 1]++;
            for (int i = 1; i <= ALFABETO; i++) conteo[i] += conteo[i - 1];
            Sala[] salida = new Sala[a.length];
            for (Sala s : a) salida[conteo[caracter(s.codigo, pos)]++] = s;
            a = salida;
        }
        return a;
    }

    private static int caracter(String s, int pos) {
        return pos < s.length() ? s.charAt(pos) : 0;
    }

    /** LSD en base 10 sobre los ocupantes; el dígito se invierte (9 - d) para orden descendente. */
    private static Sala[] ordenarPorOcupantesDesc(Sala[] a) {
        int max = 0;
        for (Sala s : a) if (s.ocupantes > max) max = s.ocupantes;
        for (int exp = 1; max / exp > 0; exp *= 10) {
            int[] conteo = new int[11];
            for (Sala s : a) conteo[9 - (s.ocupantes / exp) % 10 + 1]++;
            for (int i = 1; i <= 10; i++) conteo[i] += conteo[i - 1];
            Sala[] salida = new Sala[a.length];
            for (Sala s : a) salida[conteo[9 - (s.ocupantes / exp) % 10]++] = s;
            a = salida;
        }
        return a;
    }
}
