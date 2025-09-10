package parcial2.Iface;

import parcial2.Model.Nodo;

public interface ILinkedList<T> {
    void agregarFinal(Nodo<T> nodo);
    void intercambiarNodos();
    void mostrar();
}
