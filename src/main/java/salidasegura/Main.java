package salidasegura;

/** Ejecuta las tres salidas pedidas en el examen con los datos actuales. */
public class Main {

    public static void main(String[] args) {
        Sala[] salas = CargadorCsv.salas("/salas.csv");
        ArbolEdificio edificio = new ArbolEdificio(salas);

        System.out.println("=== F1. Personas por ala y en todo el edificio ===");
        for (String ala : edificio.alas()) {
            System.out.printf("%-8s %4d%n", ala, edificio.ocupantes(ala));
        }
        System.out.printf("%-8s %4d%n", "Edificio", edificio.ocupantesEdificio());

        System.out.println();
        System.out.println("=== F2. Reporte para la brigada (alarma activada) ===");
        Sala[] reporte = edificio.activarAlarma();
        for (int i = 0; i < reporte.length; i++) {
            Sala s = reporte[i];
            System.out.printf("%d. %-8s %-24s %-8s %3d%n", i + 1, s.codigo, s.nombre, s.ala, s.ocupantes);
        }

        System.out.println();
        System.out.println("=== F3. Guía de evacuación ===");
        Dijkstra guia = new Dijkstra(new Grafo(CargadorCsv.pasillos("/pasillos.csv")), Dijkstra.SALIDAS);
        for (Sala s : salas) {
            String[] ruta = guia.ruta(s.codigo);
            if (ruta.length == 0) {
                System.out.printf("%-8s sin ruta disponible%n", s.codigo);
                continue;
            }
            System.out.printf("%-8s -> %-12s %3d s   ruta: %s%n",
                s.codigo, guia.salida(s.codigo), guia.segundos(s.codigo), String.join(" -> ", ruta));
        }
    }
}
