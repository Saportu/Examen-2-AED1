package salidasegura;

/** Conexión entre dos lugares del edificio; se recorre en ambos sentidos. */
public class Pasillo {
    public final String desde;
    public final String hacia;
    public final int segundos;
    public final String estado;

    public Pasillo(String desde, String hacia, int segundos, String estado) {
        this.desde = desde;
        this.hacia = hacia;
        this.segundos = segundos;
        this.estado = estado;
    }

    /** F3 RNF-02: pasillo cerrado o reportado como bloqueado. */
    public boolean bloqueado() {
        String e = estado.toLowerCase();
        return e.startsWith("bloqueado") || e.startsWith("cerrado");
    }
}
