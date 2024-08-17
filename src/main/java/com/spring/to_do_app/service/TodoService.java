package com.spring.to_do_app.service;

import com.spring.to_do_app.dto.TodoDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TodoService {
    ResponseEntity<List<TodoDto>> addTodosOfUser(String projectName, List<TodoDto> todoDto);
}
