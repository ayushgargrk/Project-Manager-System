package com.spring.to_do_app.service;

import com.spring.to_do_app.dto.ProjectDto;
import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.ProjectUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {
    ResponseEntity<ProjectDto> createTheProject(ProjectDto projectDto);
    ResponseEntity<List<ProjectResponseDto>> getAllProjects();
    ResponseEntity<ProjectResponseDto> getTheProjectWithName(String name);
    ResponseEntity<List<ProjectUserDto>> addAllUserToProject(String name, List<ProjectUserDto> projectUserDto);
}
