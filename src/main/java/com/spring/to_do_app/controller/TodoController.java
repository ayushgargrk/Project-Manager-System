package com.spring.to_do_app.controller;

import com.spring.to_do_app.dto.TodoDto;
import com.spring.to_do_app.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todo")
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService){
        this.todoService = todoService;
    }

    @PostMapping("add/{name}")
    public ResponseEntity<List<TodoDto>> addTodosOfUser(@PathVariable("name") String name,
                                                        @RequestBody List<TodoDto> todoDto){
        return todoService.addTodosOfUser(name, todoDto);
    }
}
