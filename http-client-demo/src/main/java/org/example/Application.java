package org.example;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.jsonplaceholder.JsonPlaceholderHttpSession;
import org.example.jsonplaceholder.JsonPlaceholderService;
import org.example.jsonplaceholder.model.Todo;

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

        for (int i = 0; i < n; i++) {
            final int todoId = i + 1;
            try (var session = new JsonPlaceholderHttpSession()) {
                Todo todo = this.jsonPlaceholderService.todoGet(session, todoId);
                System.err.println(todo);
            }
        }
    }

    private void runSyncReuseClientDemo(int n) {
        System.err.println("=====================");
        System.err.println("= Sync Reuse Client =");
        System.err.println("=====================");
    }

    private void runAsyncReuseClientDemo(int n) {
        System.err.println("======================");
        System.err.println("= Async Reuse Client =");
        System.err.println("======================");
    }
}
