package org.example.jsonplaceholder;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;

public class JsonPlaceholderHttpSession implements AutoCloseable {

    private HttpClient httpClient;

    public JsonPlaceholderHttpSession() {
        //TODO Check how to use executor and if we need it to be managed here or by the caller
        var start = Instant.now();
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        System.err.printf("Connection Elapsed Time: %dms%n", Duration.between(start, Instant.now()).toMillis());
    }

    @Override
    public void close() throws Exception {
        httpClient = null;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
