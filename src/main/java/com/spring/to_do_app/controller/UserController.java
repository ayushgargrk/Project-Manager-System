package com.spring.to_do_app.controller;

import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("projects")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjectsOfUser(){
        return userService.getAllProjectsOfUser();
    }
}
