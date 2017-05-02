package com.example.resourceserver.web.controller;

import com.example.resourceserver.oauth.ScopeType;
import com.example.resourceserver.service.TodoService;
import com.example.resourceserver.service.dto.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * TODOを管理するコントローラークラス。
 */
@Path("/todos")
@ApplicationScoped
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Inject
    TodoService todoService;

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @RolesAllowed(ScopeType.READ_STRING)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<Todo> todoList = todoService.findAll(securityContext.getUserPrincipal().getName());
        return Response.ok(todoList).build();
    }

    @RolesAllowed(ScopeType.WRITE_STRING)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Todo todo) {
        todo.setUuid(UUID.randomUUID());
        todo.setUsername(securityContext.getUserPrincipal().getName());
        logger.info("TODOを追加します : {}", todo);
        todoService.add(todo);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(todo.getUuid().toString())
                .build();
        return Response.created(location).build();
    }
}
