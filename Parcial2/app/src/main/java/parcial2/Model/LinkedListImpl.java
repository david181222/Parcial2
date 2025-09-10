package parcial2.Model;

import parcial2.Iface.ILinkedList;

// Implementación de una lista enlazada simple usando parametrización que implementa la interfaz ILinkedList
public class LinkedListImpl<T> implements ILinkedList<T> {
    public Nodo<T> cabeza;
    public int counter; // Contador de nodos en la lista

    @Override
    public void agregarFinal(Nodo<T> nodo) {
        if (cabeza == null) {
            cabeza = nodo;   // Si la lista esta vacía, el nuevo nodo es la cabeza
        } else {
            Nodo<T> puntero = cabeza;
            while (puntero.getSiguiente() != null) {    // Recorremos la lista hasta el final
                puntero = puntero.getSiguiente();
            }
            puntero.setSiguiente(nodo);
        }
        counter++;
    }

    @Override
    public void intercambiarNodos() {
        // Validación para ver si la lista esta vacía o tiene un solo nodo
        if (cabeza == null || cabeza.getSiguiente() == null)
            return;

        Nodo<T> anterior = null; // Apunta al nodo anterior al par que se está intercambiando
        Nodo<T> actual = cabeza; // Apunta al primer nodo del par actual
        Nodo<T> siguiente = null; // Declaración fuera del ciclo

        // El segundo nodo será la nueva cabeza, porque tras el primer intercambio, el
        // segundo nodo avanza a la primera posición
        cabeza = cabeza.getSiguiente();

        // Recorremos la lista mientras haya al menos un par de nodos para intercambiar
        while (actual != null && actual.getSiguiente() != null) { 
            siguiente = actual.getSiguiente(); // Apunta al segundo nodo del par

            // El primer nodo apunta al nodo después del par que tengo
            actual.setSiguiente(siguiente.getSiguiente());

            // El segundo nodo apunta al primero, para que se de el intercambio
            siguiente.setSiguiente(actual);

            // Validamos que anterior no sea null (es decir, que no estemos en el primer
            // par)
            // Si no es null, el nodo anterior al par actual debe apuntar al segundo nodo
            if (anterior != null) {
                anterior.setSiguiente(siguiente);
            }

            // Avanzamos anterior al final del par intercambiado
            anterior = actual;
            // Avanzamos actual al siguiente par
            actual = actual.getSiguiente();
        }
    }

    @Override
    public void mostrar() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.print(actual.getDato() + " -> ");
            actual = actual.getSiguiente();
        }
        System.out.println("null");
    } 

}