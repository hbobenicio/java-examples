package org.example.jsonplaceholder;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;

public class JsonPlaceholderHttpSession implements AutoCloseable {

    private HttpClient httpClient;

    public JsonPlaceholderHttpSession() {
        var start = Instant.now();
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        System.err.printf("HttpClient Creation Time: %dms%n", Duration.between(start, Instant.now()).toMillis());
    }

    public JsonPlaceholderHttpSession(ExecutorService executorService) {
        var start = Instant.now();
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(executorService)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        System.err.printf("HttpClient Creation Time: %dms%n", Duration.between(start, Instant.now()).toMillis());
    }

    @Override
    public void close() {
        httpClient.executor()
                .map(exec -> (ExecutorService) exec)
                .ifPresent(ExecutorService::shutdown);
        httpClient = null;

        // Just for testing purposes...
//        System.gc();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}
