package org.example;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.jsonplaceholder.JsonPlaceholderHttpSession;
import org.example.jsonplaceholder.JsonPlaceholderService;
import org.example.jsonplaceholder.model.Todo;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class Application {

    private final JsonMapper jsonMapper = JsonMapper.builder()
            .addModules(new Jdk8Module())
            .addModules(new JavaTimeModule())
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .build();

    private final JsonPlaceholderService jsonPlaceholderService = new JsonPlaceholderService(jsonMapper);

    public Application() {}
    
    public void run() throws Exception {
        int n = 3;
        runSyncRecreateClientDemo(n);
        runSyncReuseClientDemo(n);
        runAsyncReuseClientDemo(n);
    }

    private void runSyncRecreateClientDemo(int n) throws Exception {
        System.err.println("========================");
        System.err.println("= Sync Recreate Client =");
        System.err.println("========================");

        var start = Instant.now();
        for (int i = 0; i < n; i++) {
            final int todoId = i + 1;
            try (var session = new JsonPlaceholderHttpSession()) {
                Todo todo = this.jsonPlaceholderService.todoGet(session, todoId);
                System.err.println(todo);
            }
        }
        System.err.printf("Total Elapsed Time: %dms%n%n", Duration.between(start, Instant.now()).toMillis());
    }

    private void runSyncReuseClientDemo(int n) throws Exception {
        System.err.println("=====================");
        System.err.println("= Sync Reuse Client =");
        System.err.println("=====================");

        var start = Instant.now();
        try (var session = new JsonPlaceholderHttpSession()) {
            for (int i = 0; i < n; i++) {
                final int todoId = i + 1;
                Todo todo = this.jsonPlaceholderService.todoGet(session, todoId);
                System.err.println(todo);
            }
        }
        System.err.printf("Total Elapsed Time: %dms%n%n", Duration.between(start, Instant.now()).toMillis());
    }

    private void runAsyncReuseClientDemo(int n) throws Exception {
        System.err.println("======================");
        System.err.println("= Async Reuse Client =");
        System.err.println("======================");

        var start = Instant.now();

        ExecutorService executorService = Executors.newFixedThreadPool(n);
        try (var session = new JsonPlaceholderHttpSession(executorService)) {

            var todosFutures = new CompletableFuture[n];

            for (int i = 0; i < n; i++) {
                final int todoId = i + 1;
                todosFutures[i] = this.jsonPlaceholderService.todoGetAsync(session, todoId)
                        .thenAccept(System.err::println);
            }

            CompletableFuture.allOf(todosFutures).join();
//        CompletableFuture.allOf(todosFutures).get(30, TimeUnit.SECONDS);
        }
        System.err.printf("Total Elapsed Time: %dms%n%n", Duration.between(start, Instant.now()).toMillis());
    }
}
