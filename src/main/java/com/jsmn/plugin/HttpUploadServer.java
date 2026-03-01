package com.jsmn.plugin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HttpUploadServer {

    private static final Pattern VALID_NAME = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final ScriptManager scriptManager;
    private final Server bukkitServer;
    private final Logger logger;
    private final String apiKey;
    private HttpServer server;

    public HttpUploadServer(ScriptManager scriptManager, Server bukkitServer, Logger logger, int port, String apiKey) throws IOException {
        this.scriptManager = scriptManager;
        this.bukkitServer = bukkitServer;
        this.logger = logger;
        this.apiKey = apiKey;

        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/upload", this::handleUpload);
        server.createContext("/", this::handlePage);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        logger.info("HTTP upload server started on port " + port);
    }

    private void handlePage(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 405, "text/plain", "Method not allowed.");
            return;
        }
        try (InputStream in = getClass().getResourceAsStream("/upload.html")) {
            if (in == null) {
                respond(exchange, 404, "text/plain", "Upload page not found.");
                return;
            }
            byte[] bytes = in.readAllBytes();
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        }
    }

    private void handleUpload(HttpExchange exchange) throws IOException {
        // CORS headers so browser-based GUIs can reach this endpoint
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "X-Api-Key, Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 204, "");
            return;
        }

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 405, "Method not allowed. Use POST.");
            return;
        }

        // API key check
        if (!apiKey.isEmpty()) {
            String provided = exchange.getRequestHeaders().getFirst("X-Api-Key");
            if (!apiKey.equals(provided)) {
                respond(exchange, 403, "Forbidden: invalid or missing X-Api-Key header.");
                return;
            }
        }

        // Parse query parameters
        Map<String, String> params = parseQuery(exchange.getRequestURI());
        String player = params.get("player");
        String name   = params.get("name");

        if (player == null || player.isBlank()) {
            respond(exchange, 400, "Missing query parameter: player");
            return;
        }
        if (name == null || name.isBlank()) {
            respond(exchange, 400, "Missing query parameter: name");
            return;
        }
        if (!VALID_NAME.matcher(player).matches()) {
            respond(exchange, 400, "Invalid player name.");
            return;
        }
        if (!VALID_NAME.matcher(name).matches()) {
            respond(exchange, 400, "Invalid script name. Use only letters, numbers, underscores and hyphens.");
            return;
        }

        // Check the player is currently logged in to the server
        if (bukkitServer.getPlayerExact(player) == null) {
            respond(exchange, 403, "Player '" + player + "' is not logged in to the server.");
            return;
        }

        // Read body as script content
        String content;
        try (InputStream in = exchange.getRequestBody()) {
            content = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }

        if (content.isBlank()) {
            respond(exchange, 400, "Script content is empty.");
            return;
        }

        // Save
        try {
            scriptManager.saveScript(name, content, player);
            logger.info("Script uploaded: " + player + "/" + name + ".js");
            respond(exchange, 200, "OK: script '" + name + "' saved to " + player + "/" + name + ".js");
        } catch (IOException e) {
            respond(exchange, 500, "Failed to save script: " + e.getMessage());
        }
    }

    private void respond(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void respond(HttpExchange exchange, int status, String body) throws IOException {
        respond(exchange, status, "text/plain", body);
    }

    private Map<String, String> parseQuery(URI uri) {
        Map<String, String> params = new HashMap<>();
        String query = uri.getQuery();
        if (query == null) return params;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            }
        }
        return params;
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            logger.info("HTTP upload server stopped.");
        }
    }
}
