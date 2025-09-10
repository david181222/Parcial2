package parcial2.Model;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Clase que define un nodo para una lista enlazada usando parametrización 
public class Nodo<T> {
    private static final Logger logger = LogManager.getLogger(Nodo.class);
    private T dato;
    private Nodo<T> siguiente; // Puntero

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
        logger.info("Nodo creado con dato: {}", dato);
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        logger.info("Actualizando dato del nodo de {} a {}", this.dato, dato);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Nodo<?> nodo = (Nodo<?>) obj;
        return dato != null ? dato.equals(nodo.dato) : nodo.dato == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dato);
    }
}