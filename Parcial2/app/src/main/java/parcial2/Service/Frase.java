
package parcial2.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;
import parcial2.Util.ApiDataFetcher;

// Esta clase tiene por objetivo manejar las frases y encriptarlas, guardandolas en listas enlazadas que se almacenan en un ArrayDeque
public class Frase {

    private static final Logger logger = LogManager.getLogger(Frase.class);

    private static List<String> quotes;
    private static ArrayDeque<LinkedListImpl<Integer>> encriptedQuotes = new ArrayDeque<>();
    private static ArrayDeque<LinkedListImpl<Integer>> encriptedSwappedQuotes = new ArrayDeque<>();
    private List<String> originalWords = new ArrayList<>();

    public List<String> getQuotes() {
        return quotes;
    }

    public Frase() {
        quotes = ApiDataFetcher.fetchDefaultData();
    }

    // Recibimos las frases ya procesadas, para recorrerlas y encriptarlas por
    // caracter usando el método encriptWord
    public void encriptQuote(List<String[]> processedQuotes) {
        LinkedListImpl<Integer> auxLinkedList = null;
        originalWords.clear();
        for (String[] words : processedQuotes) {
            for (String word : words) {
                originalWords.add(word);
                auxLinkedList = encriptWord(word);
                encriptedQuotes.add(auxLinkedList);
                logger.info("Palabra encriptada: {}", word);
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
        for (int i = 0; i < word.length(); i++) {
            auxChar = word.charAt(i);
            auxAsciiValue = ((int) auxChar) + auxCounter;
            auxCounter += 2;
            auxNodo = new Nodo<>(auxAsciiValue);
            auxLinkedList.agregarFinal(auxNodo);
        }
        return auxLinkedList;
    }

    // Intercambiar nodos adyacentes de cada lista enlazada y almacena en el
    // ArrayDeque

    public void encriptSwappedQuotes() {
        LinkedListImpl<Integer> encriptedQuote = null;
        for (LinkedListImpl<Integer> quote : encriptedQuotes) {
            encriptedQuote = quote;
            encriptedQuote.intercambiarNodos();
            encriptedSwappedQuotes.add(encriptedQuote);
        }
    }

    public void showSwappedQuotes() {
        System.out.println("Mostrando frases encriptadas e intercambiadas:");
        LinkedListImpl<Integer> quote = null;
        for (LinkedListImpl<Integer> q : encriptedSwappedQuotes) {
            quote = q;
            quote.mostrar();
        }
    }
}
