package parcial2.Service;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProcesadorFrasesService {
    private static final Logger logger = LogManager.getLogger(ProcesadorFrasesService.class);

    // El objetivo de este método es tomar las frases y devolver una lista de
    // arreglos de palabras ya procesadas
    public static List<String[]> getProcessedWords(List<String> quotes) {
        logger.info("Iniciando procesamiento de frases");
        List<String[]> processedWords = new ArrayList<>();
        if (quotes == null) {
            logger.warn("Lista de frases es null");
            return processedWords;
        }
        if (quotes.isEmpty()) {
            logger.warn("Lista de frases vacía");
            return processedWords;
        }
        int count = 0;
        for (String quote : quotes) {
            if (quote == null || quote.isBlank()) {
                logger.warn("Frase nula o vacía encontrada; se omite");
                continue;
            }
            quote = quote.replace("\"", ""); // Eliminamos comillas, vamos a tomar solo la frase como tal
            processedWords.add(quote.split(" "));
            count++;
        }
        logger.info("Procesamiento de frases completado. Total procesadas: {}", count);
        return processedWords;
    }
}
