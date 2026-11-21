package salidasegura;

/** Una sala del edificio. */
public class Sala {
    public final String codigo;
    public final String nombre;
    public final String ala;
    public int ocupantes;

    public Sala(String codigo, String nombre, String ala, int ocupantes) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.ala = ala;
        this.ocupantes = ocupantes;
    }
}
