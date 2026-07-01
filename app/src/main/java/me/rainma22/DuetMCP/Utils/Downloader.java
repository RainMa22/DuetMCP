package me.rainma22.DuetMCP.Utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 *
 */
public class Downloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36 Edg/134.0.0.0";
    private static HttpClient client = null;

    public static String get(URI uri) throws IOException, InterruptedException {
        return get(uri, Map.of());
    }

    public static String get(URI uri, Map<String, String> header) throws IOException, InterruptedException {
        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        var builder = HttpRequest
                .newBuilder(uri)
                .header("User-Agent", USER_AGENT);
        header.forEach((k, v) -> builder.header(k, v));
        var req = builder.GET().build();
        HttpResponse<String> response = client.send(req,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(String.format("[%s %s] %d", "GET", uri, response.statusCode()));
        if (response.statusCode() / 100 == 2) {
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return get(uri, header);
            }
        } else if (response.statusCode() / 100 == 3) {
            return get(uri.resolve(URI.create(response.headers().firstValue("location").orElse(""))));
        }
        throw new IOException("Unexpected status code: " + response.statusCode() + System.lineSeparator()
                + response.body());

    }

    public static String post(URI uri, Map<String, String> header, String Data) throws IOException, InterruptedException {
        if (client == null) {
            client = HttpClient.newHttpClient();
        }
        var builder = HttpRequest
                .newBuilder(uri)
                .header("User-Agent", USER_AGENT);
        header.forEach((k, v) -> builder.header(k, v));
        var req = builder.POST(HttpRequest.BodyPublishers.ofString(Data))
                .build();
        HttpResponse<String> response = client.send(req,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(String.format("[%s %s] %d", "POST", uri, response.statusCode()));
        return response.body();

    }

}
