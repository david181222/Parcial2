package parcial2.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.ArrayDeque;

import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;
import parcial2.Util.PerformanceMonitor;


// Esta clase tiene por objetivo manejar las frases y encriptarlas, guardandolas en listas enlazadas que se almacenan en un ArrayDeque
public class Frase {

    private static final Logger logger = LogManager.getLogger(Frase.class);
    private static PerformanceMonitor performanceMonitorFrase = new PerformanceMonitor("Monitoreo clase Frase");
    
    private List<String> quotes; // Ahora estado por instancia
    private final ArrayDeque<LinkedListImpl<Integer>> encriptedSwappedQuotes = new ArrayDeque<>(); 

    // Constructor que recibe las frases obtenidas desde la API (se le pasan desde App.java)
    public Frase(List<String> quotes) {
        try {
            if (quotes == null) {
                logger.warn("Constructor Frase recibió lista null");
                this.quotes = null;
            } else if (quotes.isEmpty()) {
                logger.warn("Constructor Frase recibió lista vacía");
                this.quotes = null;
            } else {
                this.quotes = quotes; 
                logger.info("Constructor Frase inicializado con {} frases", this.quotes.size());
            }
        } catch (Exception e) {
            logger.warn("Error en constructor Frase(List<String>): {}", e.getMessage());
            this.quotes = null;
            throw e;
        }
    }


    // Recibimos las frases ya procesadas, para recorrerlas y encriptarlas por
    // caracter usando el método encriptWord
    public void encriptQuote(List<String[]> processedQuotes) {
        performanceMonitorFrase.inicio(); // Start monitoring performance
        logger.info("Recibiendo frases procesadas para encriptar y swappear nodos");
        if (processedQuotes == null) {
            logger.warn("Lista de frases procesadas es null. Abandonando encriptación");
            return;
        }
        if (processedQuotes.isEmpty()) {
            logger.warn("Lista de frases procesadas vacía. Nada que encriptar");
            return;
        }
        int palabrasProcesadas = 0;
        try {
            LinkedListImpl<Integer> auxLinkedList;
            for (String[] words : processedQuotes) {
                if (words == null || words.length == 0) {
                    logger.warn("Arreglo de palabras nulo o vacío encontrado");
                    continue;
                }
                for (String word : words) {                     // Recorremos cada palabra del arreglo
                    if (word == null || word.isBlank()) {
                        logger.warn("Palabra nula o vacía encontrada");
                        continue;
                    }
                    logger.info("Encriptando palabra: {}", word);
                    auxLinkedList = encriptWord(word);          // Encriptamos la palabra en una lista enlazada usando el método encriptWord
                    auxLinkedList.intercambiarNodos();          // Intercambiamos nodos en la lista enlazada
                    encriptedSwappedQuotes.add(auxLinkedList);  // Agregamos la lista enlazada con la palabra encriptada e intercambiada al ArrayDeque
                    palabrasProcesadas++;                       // Contador para manejo de logs
                }
            }
            logger.info("Proceso de encriptación completado. Total palabras encriptadas: {}", palabrasProcesadas);
        } catch (Exception e) {
            logger.warn("Error durante la encriptación de frases: {}", e.getMessage());
            throw e; 
        } finally {
            performanceMonitorFrase.finalizado(); 
        }
    }

    // Método para encriptar una palabra procesando los caracteres
    private LinkedListImpl<Integer> encriptWord(String word) {
        LinkedListImpl<Integer> auxLinkedList = new LinkedListImpl<>();
        if (word == null) {
            logger.warn("Se intentó encriptar una palabra null");
            return auxLinkedList;
        }
        if (word.isEmpty()) {
            logger.warn("Se intentó encriptar una palabra vacía");
            return auxLinkedList;
        }
        try {
            int auxCounter = 1;
            char auxChar;
            int auxAsciiValue;
            Nodo<Integer> auxNodo;
            for (int i = 0; i < word.length(); i++) {       // Recorremos cada caracter de la palabra con un ciclo for para tomar cada uno
                auxChar = word.charAt(i);                   // Obtenemos el caracter en la posición i
                auxAsciiValue = ((int) auxChar) + auxCounter;   // Convertimos el caracter a su valor ASCII y le sumamos el contador de impares
                auxCounter += 2;                               // Incrementamos el contador en 2 (para que siga en impares) para el siguiente caracter
                auxNodo = new Nodo<>(auxAsciiValue);           // Creamos un nuevo nodo con el valor encriptado
                auxLinkedList.agregarFinal(auxNodo);           // Agregamos el nodo al final de la lista enlazada, cuando vuelva a iterar va formando la palabra en la lista enlazada
            }
        } catch (Exception e) {
            logger.warn("Error en encriptWord para la palabra '{}': {}", word, e.getMessage());
        }
        return auxLinkedList;
    }

    // Método para mostrar las frases encriptadas e intercambiadas en consola (es un método para prueba y visualización)
    public void showSwappedQuotes() {
        logger.info("Mostrando frases encriptadas e intercambiadas");
        if (encriptedSwappedQuotes == null || encriptedSwappedQuotes.isEmpty()) {
            logger.warn("No hay frases encriptadas para mostrar");
            System.out.println("(Sin frases encriptadas)" );
            return;
        }
        System.out.println("Mostrando frases encriptadas e intercambiadas:");
        for (LinkedListImpl<Integer> q : encriptedSwappedQuotes) {
            if (q == null || q.cabeza == null) {
                logger.warn("Se encontró una lista vacía o nula dentro del Deque");
                continue;
            }
            q.mostrar();
        }
    }

    public List<String> getQuotes() {
        return quotes;
    }

    public ArrayDeque<LinkedListImpl<Integer>> getEncriptedSwappedQuotes() {
        return encriptedSwappedQuotes;
    }
}
