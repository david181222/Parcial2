package parcial2.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApiDataFetcher {
    private static final Logger logger = LogManager.getLogger(ApiDataFetcher.class);
    public static List<String> fetchAndSelect(String apiUrl, Set<String> camposSeleccionados) throws Exception {
        logger.info("Realizando petición a la API: {}", apiUrl);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        if (status != 200) {
            logger.warn("Respuesta no exitosa de la API. Código: {}", status);
        } else {
            logger.info("Respuesta exitosa de la API (200)");
        }
        Gson gson = new Gson();
        List<String> resultado = new ArrayList<>();
        JsonElement jsonElement = gson.fromJson(response.body(), JsonElement.class);
        if (jsonElement != null && jsonElement.isJsonArray()) {
            for (JsonElement elem : jsonElement.getAsJsonArray()) {
                if (elem.isJsonObject()) {
                    JsonObject original = elem.getAsJsonObject();
                    for (String campo : camposSeleccionados) {
                        if (original.has(campo)) {
                            resultado.add(original.get(campo).toString());
                        }
                    }
                }
            }
        } else {
            logger.warn("Formato de respuesta inesperado: no es un array JSON");
        }
        logger.info("Total de elementos obtenidos: {}", resultado.size());
        return resultado;
    }

    // Método utilitario para obtener datos de la API ZenQuotes con campos relevantes
    public static List<String> fetchDefaultData() {
        try {
            String apiUrl = "https://zenquotes.io/api/quotes";
            Set<String> campos = Set.of("q");
            List<String> data = fetchAndSelect(apiUrl, campos);
            if (data == null || data.isEmpty()) {
                logger.warn("No se recibieron datos desde la API");
            } else {
                logger.info("Datos recibidos correctamente. Total: {}", data.size());
            }
            return data;
        } catch (Exception e) {
            logger.warn("Error al consumir la API: {}", e.getMessage());
            return null;
        }
    }
}
