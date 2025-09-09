package parcial2.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApiDataFetcher {
    public static List<String> fetchAndSelect(String apiUrl, Set<String> camposSeleccionados) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        List<String> resultado = new ArrayList<>();
        // La API de ZenQuotes devuelve un array en la raíz
        JsonElement jsonElement = gson.fromJson(response.body(), JsonElement.class);
        if (jsonElement.isJsonArray()) {
            for (JsonElement elem : jsonElement.getAsJsonArray()) {
                if (elem.isJsonObject()) {
                    JsonObject original = elem.getAsJsonObject();
                    List<String> filtrado = new ArrayList<>();
                    for (String campo : camposSeleccionados) {
                        if (original.has(campo)) {
                            resultado.add(original.get(campo).toString());
                        }
                    }
                }
            }
        }
        return resultado;
    }

    // Método utilitario para obtener datos de la API ZenQuotes con campos relevantes
    public static List<String> fetchDefaultData() {
        try {
            String apiUrl = "https://zenquotes.io/api/quotes";
        Set<String> campos = Set.of("q");
        return fetchAndSelect(apiUrl, campos);
        } catch (Exception e) {
           return null;
        }
    }
}
