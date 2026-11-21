package salidasegura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/** Lee los CSV de datos (desde el classpath) a arreglos propios. */
public final class CargadorCsv {

    private CargadorCsv() {}

    public static Sala[] salas(String recurso) {
        String[] lineas = leerLineas(recurso);
        Sala[] salas = new Sala[lineas.length];
        for (int i = 0; i < lineas.length; i++) {
            String[] c = lineas[i].split(",");
            if (c.length != 4) throw new IllegalArgumentException("Línea inválida: " + lineas[i]);
            salas[i] = new Sala(c[0].trim(), c[1].trim(), c[2].trim(), Integer.parseInt(c[3].trim()));
        }
        return salas;
    }

    public static Pasillo[] pasillos(String recurso) {
        String[] lineas = leerLineas(recurso);
        Pasillo[] pasillos = new Pasillo[lineas.length];
        for (int i = 0; i < lineas.length; i++) {
            String[] c = lineas[i].split(",");
            if (c.length != 4) throw new IllegalArgumentException("Línea inválida: " + lineas[i]);
            pasillos[i] = new Pasillo(c[0].trim(), c[1].trim(), Integer.parseInt(c[2].trim()), c[3].trim());
        }
        return pasillos;
    }

    /** Devuelve las líneas no vacías sin el encabezado. */
    private static String[] leerLineas(String recurso) {
        InputStream in = CargadorCsv.class.getResourceAsStream(recurso);
        if (in == null) throw new IllegalArgumentException("No existe el recurso " + recurso);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String[] lineas = new String[8];
            int n = 0;
            br.readLine(); // encabezado
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) continue;
                if (n == lineas.length) lineas = crecer(lineas);
                lineas[n++] = linea;
            }
            String[] exactas = new String[n];
            for (int i = 0; i < n; i++) exactas[i] = lineas[i];
            return exactas;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + recurso, e);
        }
    }

    private static String[] crecer(String[] a) {
        String[] b = new String[a.length * 2];
        for (int i = 0; i < a.length; i++) b[i] = a[i];
        return b;
    }
}
