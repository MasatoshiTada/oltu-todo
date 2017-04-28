package com.example.resourceserver.service;

import com.example.resourceserver.service.dto.Todo;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@ApplicationScoped
public class TodoService {

    /**
     * 簡略化のためインメモリでTODOを保持する（本来はDBなどに保持する）
     */
    private final List<Todo> todoList = new CopyOnWriteArrayList<>();

    /**
     * @param username ユーザー名
     * @return ユーザーの全TODO
     */
    public List<Todo> findAll(String username) {
        return todoList.stream()
                .filter(todo -> todo.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * TODOを追加する
     */
    public void add(Todo todo) {
        todoList.add(todo);
    }
}
