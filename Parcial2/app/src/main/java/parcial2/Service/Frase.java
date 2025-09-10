
package parcial2.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayDeque;

import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;
import parcial2.Util.ApiDataFetcher;

// Esta clase tiene por objetivo manejar las frases y encriptarlas, guardandolas en listas enlazadas que se almacenan en un ArrayDeque
public class Frase {

    private static final Logger logger = LogManager.getLogger(Frase.class);

    private static List<String> quotes;
    private static ArrayDeque<LinkedListImpl<Integer>> encriptedSwappedQuotes = new ArrayDeque<>(); // Listas encriptadas con nodos intercambiados

    // Constructor que inicializa las frases consumiendo la API 
    public Frase() {
        quotes = ApiDataFetcher.fetchDefaultData();
        logger.info("Frases obtenidas de la API con exito");
    }


    // Recibimos las frases ya procesadas, para recorrerlas y encriptarlas por
    // caracter usando el método encriptWord
    public void encriptQuote(List<String[]> processedQuotes) {
        // Fusionamos: encriptar + intercambiar nodos inmediatamente
        LinkedListImpl<Integer> auxLinkedList;
        logger.info("Recibiendo frases procesadas para encriptar y swappear nodos");
        for (String[] words : processedQuotes) {
            for (String word : words) {
                logger.info("Encriptando palabra: {}", word);
                auxLinkedList = encriptWord(word);
                logger.info("Palabra encriptada: {}", word);
                auxLinkedList.intercambiarNodos(); // Intercambiamos nodos en el momento de la encriptación
                logger.info("Nodos intercambiados para la palabra: {}", word);
                encriptedSwappedQuotes.add(auxLinkedList);  // Añadimos la lista enlazada ya encriptada e intercambiada al ArrayDeque
                logger.info("Palabra totalmente encriptada ha sido añadida al ArrayDeque");
            
            }
        }
    }

    // Método para encriptar una palabra procesando los caracteres
    private LinkedListImpl<Integer> encriptWord(String word) {
        LinkedListImpl<Integer> auxLinkedList = new LinkedListImpl<>();
        int auxCounter = 1;
        char auxChar = 0;
        int auxAsciiValue = 0;
        Nodo<Integer> auxNodo = null;
        for (int i = 0; i < word.length(); i++) {           // Recorremos cada caracter de la palabra
            auxChar = word.charAt(i);
            auxAsciiValue = ((int) auxChar) + auxCounter;   // Encriptamos sumando el contador de impares a su valor ASCII
            auxCounter += 2;                                // Incrementamos el contador en 2 para el siguiente caracter
            auxNodo = new Nodo<>(auxAsciiValue);            // Creamos el nodo con el valor encriptado (se debe instanciar dentro del for)
            auxLinkedList.agregarFinal(auxNodo);
        }
        return auxLinkedList;
    }

    // Método eliminado: el intercambio ahora ocurre dentro de encriptQuote.

    public void showSwappedQuotes() {
        System.out.println("Mostrando frases encriptadas e intercambiadas:");
        LinkedListImpl<Integer> quote = null;
        for (LinkedListImpl<Integer> q : encriptedSwappedQuotes) {
            quote = q;
            quote.mostrar();
        }
    }

     public List<String> getQuotes() {
        return quotes;
    }

    public ArrayDeque<LinkedListImpl<Integer>> getEncriptedSwappedQuotes() {
        return encriptedSwappedQuotes;
    }
}
