package parcial2.Util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApiDataFetcher {
    public static List<JsonObject> fetchAndSelect(String apiUrl, Set<String> camposSeleccionados) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        List<JsonObject> resultado = new ArrayList<>();
        if (jsonResponse.has("data") && jsonResponse.get("data").isJsonArray()) {
            for (JsonElement elem : jsonResponse.getAsJsonArray("data")) {
                JsonObject original = elem.getAsJsonObject();
                JsonObject filtrado = new JsonObject();
                for (String campo : camposSeleccionados) {
                    if (original.has(campo)) {
                        filtrado.add(campo, original.get(campo));
                    }
                }
                resultado.add(filtrado);
            }
        }
        return resultado;
    }

    // Método utilitario para obtener datos de la API de criptomonedas con campos predefinidos
    public static List<JsonObject> fetchDefaultData() throws Exception {
        String apiUrl = "https://api.coinlore.net/api/tickers/";
        Set<String> campos = Set.of("id", "name", "price_usd");
        return fetchAndSelect(apiUrl, campos);
    }
}
