package parcial2.Iface;

import parcial2.Model.Nodo;

// Esta interfaz define los métodos obligatorios para la implementación de la lista enlazada
public interface ILinkedList<T> {
    void agregarFinal(Nodo<T> nodo);
    void intercambiarNodos();
    void mostrar();
}
