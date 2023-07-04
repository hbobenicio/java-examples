package org.example.jsonplaceholder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.jsonplaceholder.model.Todo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static java.lang.String.format;

public class JsonPlaceholderService {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private final JsonMapper jsonMapper;

    public JsonPlaceholderService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public Todo todoGet(JsonPlaceholderHttpSession session, int todoId) throws IOException, InterruptedException {
        HttpRequest req = todoGetRequest(todoId);
        System.err.printf("Sending Request: %s %s%n", req.method(), req.uri());

        var start = Instant.now();
        HttpResponse<String> res = session.getHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        System.err.printf("Request Elapsed Time: %dms%n", Duration.between(start, Instant.now()).toMillis());

        if (res.statusCode() != 200) {
            throw unexpectedStatusCode(res.statusCode());
        }

        return todoParse(res.body());
    }

    private Todo todoParse(String s) throws JsonProcessingException {
        return this.jsonMapper.readValue(s, Todo.class);
    }

    private RuntimeException unexpectedStatusCode(int statusCode) {
        var errorMessage = format("Unexpected HTTP Status Code: %d", statusCode);
        return new RuntimeException(errorMessage);
    }

    private HttpRequest todoGetRequest(int todoId) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(format("%s/todos/%d", BASE_URL, todoId)))
                .timeout(Duration.ofSeconds(10))
                .build();
    }
}
