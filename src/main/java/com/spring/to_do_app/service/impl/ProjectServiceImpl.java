package com.spring.to_do_app.service.impl;

import com.spring.to_do_app.constants.Mails;
import com.spring.to_do_app.constants.constants;
import com.spring.to_do_app.dto.ProjectDto;
import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.ProjectUserDto;
import com.spring.to_do_app.entities.ProjectEntity;
import com.spring.to_do_app.entities.UserEntity;
import com.spring.to_do_app.repositories.ProjectRepository;
import com.spring.to_do_app.repositories.UserRepository;
import com.spring.to_do_app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository,
                              EmailService emailService){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<ProjectDto> createTheProject(ProjectDto projectDto) {
        Boolean projectExists = projectRepository.existsByName(projectDto.getName());
        ProjectDto response = new ProjectDto();
        if(projectExists){
            response.setError(constants.PROJECT_EXISTS);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();

        ProjectEntity project = ProjectEntity.builder()
                .name(projectDto.getName())
                .description(projectDto.getDescription())
                .done(false)
                .author(user.getFirstName() + " " + (user.getLastName() != null? user.getLastName(): ""))
                .authorEmail(authentication.getName())
                .users(List.of(user))
                .build();

        projectRepository.save(project);
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setAuthor(project.getAuthor());

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<ProjectEntity> projects = projectRepository.findAllByAuthorEmail(authentication.getName());
        return new ResponseEntity<>(mapToProjectResponseDto(projects),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProjectResponseDto> getTheProjectWithName(String name) {
        Boolean projectExists = projectRepository.existsByName(name);
        if(!projectExists){
            ProjectResponseDto responseDto = new ProjectResponseDto();
            responseDto.setError(constants.PROJECT_NOT_EXISTS);
            return new ResponseEntity<>(responseDto,HttpStatus.BAD_REQUEST);
        }

        ProjectEntity project = projectRepository.findByName(name).get();
        ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .name(project.getName())
                .author(project.getAuthor())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .users(mapUserEntityToUserDto(project.getUsers()))
                .build();
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ProjectUserDto>> addAllUserToProject(String name, List<ProjectUserDto> projectUserDto) {
        Boolean projectExists = projectRepository.existsByName(name);
        if(!projectExists){
            ProjectUserDto responseDto = new ProjectUserDto();
            responseDto.setError(constants.PROJECT_NOT_EXISTS);
            return new ResponseEntity<>(List.of(responseDto),HttpStatus.BAD_REQUEST);
        }

        ProjectEntity project = projectRepository.findByName(name).get();
        List<UserEntity> userEntities = mapUserDtoToUserEntity(project,projectUserDto);
        project.getUsers().addAll(userEntities);

        projectRepository.save(project);

        return new ResponseEntity<>(mapUserEntityToUserDto(userEntities),HttpStatus.OK);
    }

    private List<UserEntity> mapUserDtoToUserEntity(ProjectEntity project, List<ProjectUserDto> userDto) {
        return userDto.stream().map((user) -> {
            Boolean userExists = userRepository.existsByEmail(user.getEmail());
            if(!userExists){
                return null;
            }

            UserEntity userEntity = userRepository.findByEmail(user.getEmail()).get();
            Mails.Email email = Mails.addedToGroup(user.getEmail(),project.getName(),project.getAuthor());
            emailService.sendMail(email.getTo(),email.getSubject(),email.getBody());
            return userEntity;
        }).collect(Collectors.toList());
    }

    private List<ProjectUserDto> mapUserEntityToUserDto(List<UserEntity> users) {
        return users.stream().map((user) -> ProjectUserDto.builder()
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProjectResponseDto> mapToProjectResponseDto(List<ProjectEntity> projects) {
        return projects.stream().map((project) -> ProjectResponseDto.builder()
                .name(project.getName())
                .author(project.getAuthor())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .status(project.getDone()? constants.COMPLETED: constants.IN_PROGRESS)
                .build()).collect(Collectors.toList());
    }

}
