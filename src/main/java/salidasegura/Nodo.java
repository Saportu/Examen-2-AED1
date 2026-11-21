package salidasegura;

/**
 * Nodo del árbol n-ario: edificio (raíz), ala o sala (hoja).
 * Cada nodo guarda el total de ocupantes de su subárbol.
 */
class Nodo {
    final String nombre;
    final Nodo padre;
    final Sala sala; // solo en hojas
    Nodo[] hijos = new Nodo[2];
    int cantidadHijos;
    int total;

    Nodo(String nombre, Nodo padre, Sala sala) {
        this.nombre = nombre;
        this.padre = padre;
        this.sala = sala;
    }

    Nodo agregarHijo(Nodo hijo) {
        if (cantidadHijos == hijos.length) {
            Nodo[] nuevos = new Nodo[hijos.length * 2];
            for (int i = 0; i < cantidadHijos; i++) nuevos[i] = hijos[i];
            hijos = nuevos;
        }
        hijos[cantidadHijos++] = hijo;
        return hijo;
    }

    Nodo buscarHijo(String nombreHijo) {
        for (int i = 0; i < cantidadHijos; i++) {
            if (hijos[i].nombre.equals(nombreHijo)) return hijos[i];
        }
        return null;
    }
}
