package api;

import com.sun.net.httpserver.*;
import service.ResourceService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import model.resource.Resource;

// GetResourcesHandler exposes the resource catalog over the legacy HTTP API, following GRASP Controller.
public class GetResourcesHandler implements HttpHandler {

    @Override
    // Handles a GET request and returns available resources as JSON data.
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) return;

        ResourceService service = new ResourceService();

        // ✅ FIXED METHOD NAME
        List<Resource> resources = service.getAvailableResources();

        JSONArray arr = new JSONArray();

        for (Resource r : resources) {
            // ✅ BEST PRACTICE: use your model's built-in JSON method
            arr.put(r.toJson());
        }

        JSONObject response = new JSONObject();
        response.put("status", "success");
        response.put("data", arr);

        send(exchange, 200, response);
    }

    // Writes a JSON response so the handler keeps HTTP formatting concerns localized.
    private void send(HttpExchange ex, int code, JSONObject res) throws IOException {
        ex.getResponseHeaders().add("Content-Type", "application/json");
        byte[] bytes = res.toString().getBytes();
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }
}
