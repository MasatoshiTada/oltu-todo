package com.example.readwriteclient.web.form;

import com.example.readwriteclient.service.dto.Todo;

import javax.ws.rs.FormParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TodoForm {

    @FormParam("text")
    private String text;

    @FormParam("deadline")
    private LocalDate deadline;

    public TodoForm() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Todo convert() {
        return new Todo(text, deadline);
    }

    @Override
    public String toString() {
        return "TodoForm{" +
                "text='" + text + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
