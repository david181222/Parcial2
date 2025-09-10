package parcial2.Service;

import java.util.ArrayDeque;
import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;

// Esta clase tiene por objetivo desencriptar las frases que se encuentran en listas enlazadas dentro del ArrayDeque
// encriptedSwappedQuotes. Lo que se hace es recorrer el ArrayDeque y revertir todo el proceso de encriptación
public class DesencriptarService {

    // Este método recorre el ArrayDeque, tomando cada lista enlazada, aplicando de
    // nuevo el switcheo de nodos
    // y luego reconstruyendo la palabra original concatenándola en un mensaje
    // Decidimos usar StringBuilder para optimizar la concatenación de strings en función de evitar que se hagan copias innecesarias al 
    // modificar el String message, mientras que StringBuilder es mutable internamente, lo que lo hace ideal para modificaciones dentro de ciclos.
    public String desEncript(ArrayDeque<LinkedListImpl<Integer>> swappedEncryptedQuotes) {
    if (swappedEncryptedQuotes == null || swappedEncryptedQuotes.isEmpty()) {
        return "";
    }

    StringBuilder message = new StringBuilder();
    int wordIndex = 0;
    for (LinkedListImpl<Integer> wordOnList : swappedEncryptedQuotes) {
        wordOnList.intercambiarNodos();
        if (wordIndex > 0) {
            message.append(' ');
        }
        message.append(recoveryWord(wordOnList));
        wordIndex++;
    }
    return message.toString();
    }

    // Este método toma una palabra que esta encriptada en la lista enlazada, y con
    // un puntero recorremos cada nodo
    // aplicando los pasos contrarios a los de encriptación
    private String recoveryWord(LinkedListImpl<Integer> wordOnList) {
        String word = "";
        Nodo<Integer> actual = wordOnList.cabeza;
        int auxCounter = 1;
        int encriptNumber;
        int originalAscii;
        while (actual != null) {
            encriptNumber = actual.getDato();
            originalAscii = encriptNumber - auxCounter;
            word = word + (char) originalAscii;
            auxCounter += 2;
            actual = actual.getSiguiente();
        }
        return word;
    }
}
