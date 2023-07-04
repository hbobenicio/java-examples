package org.example;

public class Main {
    public static void main(String[] args) {
        var app = new Application();
        try {
            app.run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
