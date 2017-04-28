package com.example.readclient.web.form;

import com.example.readclient.service.dto.Todo;

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
        // FIXME ダミーの名前
        return new Todo("dummyName", text, deadline);
    }

    @Override
    public String toString() {
        return "TodoForm{" +
                "text='" + text + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}
