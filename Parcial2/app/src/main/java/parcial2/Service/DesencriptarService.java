package parcial2.Service;

import java.util.ArrayDeque;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import parcial2.Model.LinkedListImpl;
import parcial2.Model.Nodo;
import parcial2.Util.PerformanceMonitor;

// Esta clase tiene por objetivo desencriptar las frases que se encuentran en listas enlazadas dentro del ArrayDeque
// encriptedSwappedQuotes. Lo que se hace es recorrer el ArrayDeque y revertir todo el proceso de encriptación
public class DesencriptarService {

    private static final Logger logger = LogManager.getLogger(DesencriptarService.class);
    private static PerformanceMonitor performanceMonitorDesencriptarService = new PerformanceMonitor("Monitoreo clase DesencriptarService");

    // Este método recorre el ArrayDeque, tomando cada lista enlazada, aplicando de
    // nuevo el switcheo de nodos
    // y luego reconstruyendo la palabra original concatenándola en un mensaje
    // Decidimos usar StringBuilder para optimizar la concatenación de strings en función de evitar que se hagan copias innecesarias al 
    // modificar el String message, mientras que StringBuilder es mutable internamente, lo que lo hace ideal para modificaciones dentro de ciclos.
    public String desEncript(ArrayDeque<LinkedListImpl<Integer>> swappedEncryptedQuotes) {
        performanceMonitorDesencriptarService.inicio();
        logger.info("Iniciando desencriptación de frases");
        if (swappedEncryptedQuotes == null) {
            logger.warn("El ArrayDeque recibido es null");
            return "";
        }
        if (swappedEncryptedQuotes.isEmpty()) {
            logger.warn("No hay listas para desencriptar (ArrayDeque vacío)");
            return "";
        }
        StringBuilder message = new StringBuilder();
        int wordIndex = 0;
        try {
            for (LinkedListImpl<Integer> wordOnList : swappedEncryptedQuotes) {  // Recorremos cada lista enlazada en el ArrayDeque
                if (wordOnList == null || wordOnList.cabeza == null) {
                    logger.warn("Lista nula o vacía encontrada durante desencriptación; se omite");
                    continue;
                }
                wordOnList.intercambiarNodos();         // Revertimos el switcheo de nodos en la lista enlazada, para que quede como la original
                if (wordIndex > 0) {
                    message.append(' ');
                }
                String recovered = recoveryWord(wordOnList);      // Reconstruimos la palabra original pasando la lista enlazada al método recoveryWord
                logger.info("Palabra desencriptada agregada al mensaje");
                message.append(recovered);                       // Concatenamos la palabra recuperada al mensaje final
                wordIndex++;
            }
            logger.info("Desencriptación completada. Total palabras: {}", wordIndex);
        } catch (Exception e) {
            logger.warn("Error durante la desencriptación: {}", e.getMessage());
        } finally {
            performanceMonitorDesencriptarService.finalizado();
        }
        return message.toString();
    }

    // Este método toma una palabra que esta encriptada en la lista enlazada, y con
    // un puntero recorremos cada nodo
    // aplicando los pasos contrarios a los de encriptación
    private String recoveryWord(LinkedListImpl<Integer> wordOnList) {
        // Validaciones iniciales
        if (wordOnList == null) {
            logger.warn("recoverWord recibió una lista null");
            return "";
        }
        Nodo<Integer> actual = wordOnList.cabeza;
        if (actual == null) {
            logger.warn("recoverWord recibió una lista sin nodos (cabeza null)");
            return "";
        }
        StringBuilder word = new StringBuilder();
        int auxCounter = 1;
        int wordLength = 0;
        try {
            int encriptNumber;
            int originalAscii;
            Integer dato;
            while (actual != null) {
                dato = actual.getDato();
                if (dato == null) {
                    logger.warn("Nodo con dato null encontrado; se interrumpe desencriptación.");
                    break;
                }
                encriptNumber = dato;
                originalAscii = encriptNumber - auxCounter;  // Revertimos la encriptación restando el contador impar, así obtenemos su valor ASCII original
                if (originalAscii < 0 || originalAscii > Character.MAX_VALUE) { // Validamos que el valor ASCII esté en un rango válido
                    logger.warn("Valor ASCII fuera de rango ({}). Se omite nodo.", originalAscii);
                } else {
                    word.append((char) originalAscii);      // Convertimos el valor ASCII a caracter y lo concatenamos a la palabra
                    wordLength++;                               // Contador de la longitud de la palabra para información en logs
                }
                auxCounter += 2;                       // Incrementamos el contador en 2 (para que siga en impares) para el siguiente caracter
                actual = actual.getSiguiente();        // Avanzamos al siguiente nodo usando el puntero , cuando vuelva a iterar concatena el siguiente caracter formando la palabra
            }
            logger.info("Palabra desencriptada de longitud: {}", wordLength);
        } catch (Exception e) {
            logger.warn("Error en recoveryWord: {}", e.getMessage());
        }
        return word.toString();
    }
}
