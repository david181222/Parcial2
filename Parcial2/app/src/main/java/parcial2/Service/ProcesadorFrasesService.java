package parcial2.Service;

import java.util.ArrayList;
import java.util.List;

public class ProcesadorFrasesService {
    // El objetivo de este método es tomar las frases y devolver una lista de arreglos de palabras ya procesadas
    public static List<String[]> getProcessedWords(List<String> quotes) {
        List<String[]> processedWords = new ArrayList<>();
        for (String quote : quotes) {
            quote = quote.replace("\"", ""); // Eliminamos comillas si las hay
            processedWords.add(quote.split(" "));
        }
        return processedWords;
    }
}
