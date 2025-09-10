package parcial2.Model;

// Clase Nodo genérica
public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente; //Puntero

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "dato=" + dato +
                ", siguiente=" + (siguiente != null ? "Nodo{" + "dato=" + siguiente.dato + '}' : "null") +
                '}';
    }
}