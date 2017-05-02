package com.example.readwriteclient.web.controller;

import com.example.readwriteclient.service.TodoService;
import com.example.readwriteclient.service.dto.Todo;
import com.example.readwriteclient.web.filter.AccessTokenRequired;
import com.example.readwriteclient.web.form.TodoForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.Models;
import javax.mvc.annotation.Controller;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/todo")
@ApplicationScoped
@Controller
@Produces(MediaType.TEXT_HTML)
@AccessTokenRequired
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Inject
    TodoService todoService;

    @Inject
    Models models;

    @GET
    @Path("/index")
    public String index() throws Exception {
        List<Todo> todoList = todoService.findAll();
        models.put("todoList", todoList);
        return "index.html";
    }

    @POST
    @Path("/add")
    public String add(@BeanParam TodoForm todoForm) throws Exception {
        logger.info("TODOの入力を受け取りました：{}", todoForm);
        Todo todo = todoForm.convert();
        logger.info("TODOを変換しました：{}", todo);
        todoService.add(todo);
        return "redirect:/todo/index";
    }
}
