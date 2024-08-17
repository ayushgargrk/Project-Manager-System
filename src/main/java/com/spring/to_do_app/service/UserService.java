package com.spring.to_do_app.service;

import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.ProjectUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<List<ProjectResponseDto>> getAllProjectsOfUser();
}
