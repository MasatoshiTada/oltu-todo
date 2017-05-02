package com.example.resourceserver.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Todo {

    private UUID uuid;

    private String username;

    private String text;

    private LocalDateTime createdAt;

    private LocalDate deadline;

    private Boolean completed;

    public Todo() {
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", deadline=" + deadline +
                ", completed=" + completed +
                '}';
    }
}
