package org.example.jsonplaceholder.model;

public record Todo(int id, int userId, String title, boolean completed) {
}
