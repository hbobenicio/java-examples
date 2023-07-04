package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            var app = new Application();
            app.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
