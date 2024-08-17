package com.spring.to_do_app.service.impl;

import com.spring.to_do_app.constants.constants;
import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.TodoDto;
import com.spring.to_do_app.entities.ProjectEntity;
import com.spring.to_do_app.entities.TodoEntity;
import com.spring.to_do_app.entities.UserEntity;
import com.spring.to_do_app.repositories.ProjectRepository;
import com.spring.to_do_app.repositories.TodoRepository;
import com.spring.to_do_app.repositories.UserRepository;
import com.spring.to_do_app.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoServiceImpl implements TodoService {

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(UserRepository userRepository, ProjectRepository projectRepository,
                           TodoRepository todoRepository){
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.todoRepository = todoRepository;
    }

    @Override
    public ResponseEntity<List<TodoDto>> addTodosOfUser(String projectName, List<TodoDto> todoDto) {
        Boolean projectExists = projectRepository.existsByName(projectName);
        if(!projectExists){
            TodoDto error = new TodoDto();
            error.setError(constants.PROJECT_NOT_EXISTS);
            return new ResponseEntity<>(List.of(error), HttpStatus.BAD_REQUEST);
        }

        List<TodoEntity> todoEntities = saveTodosOfUser(projectName,todoDto);
        return new ResponseEntity<>(mapTodoDtoToEntity(todoEntities),HttpStatus.CREATED);
    }

    private List<TodoDto> mapTodoDtoToEntity(List<TodoEntity> todoEntities) {
        return todoEntities.stream().map((todo) -> {
            ProjectResponseDto responseDto = ProjectResponseDto.builder()
                    .name(todo.getProject().getName())
                    .description(todo.getProject().getDescription())
                    .author(todo.getProject().getAuthor())
                    .build();
            return TodoDto.builder()
                    .name(todo.getName())
                    .status(todo.getDone()? constants.COMPLETED: constants.IN_PROGRESS)
                    .createdAt(todo.getCreatedAt())
                    .project(responseDto)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<TodoEntity> saveTodosOfUser(String projectName, List<TodoDto> todoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        ProjectEntity project = projectRepository.findByName(projectName).get();

        return todoDto.stream().map((todo) -> {
            TodoEntity entity = TodoEntity.builder()
                    .name(todo.getName())
                    .done(false)
                    .user(user)
                    .project(project)
                    .build();
            todoRepository.save(entity);
            return entity;
        }).collect(Collectors.toList());
    }
}
