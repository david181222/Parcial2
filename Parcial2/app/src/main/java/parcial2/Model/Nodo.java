package parcial2.Model;

// Clase Nodo
public class Nodo {
    public int dato;
    public Nodo siguiente; //Puntero

    public Nodo(int dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "dato=" + dato +
                ", siguiente=" + (siguiente != null ? "Nodo{" + "dato=" + siguiente.dato + '}' : "null") +
                '}';
    }
    
}