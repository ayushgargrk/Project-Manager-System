package com.spring.to_do_app.controller;

import com.spring.to_do_app.dto.ProjectDto;
import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.ProjectUserDto;
import com.spring.to_do_app.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService){
        this.projectService = projectService;
    }

    @PostMapping("create")
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto){
        return projectService.createTheProject(projectDto);
    }

    @GetMapping("all")
    public ResponseEntity<List<ProjectResponseDto>> returnAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("{name}")
    public ResponseEntity<ProjectResponseDto> returnTheProject(@PathVariable("name") String name){
        return projectService.getTheProjectWithName(name);
    }

    @PostMapping("add/{name}")
    public ResponseEntity<List<ProjectUserDto>> addUserToProject(@PathVariable("name") String name,
                                                                 @RequestBody List<ProjectUserDto> projectUserDto){
        return projectService.addAllUserToProject(name, projectUserDto);
    }
}
