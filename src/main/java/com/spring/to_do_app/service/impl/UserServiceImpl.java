package com.spring.to_do_app.service.impl;

import com.spring.to_do_app.constants.constants;
import com.spring.to_do_app.dto.ProjectResponseDto;
import com.spring.to_do_app.dto.ProjectUserDto;
import com.spring.to_do_app.entities.ProjectEntity;
import com.spring.to_do_app.entities.UserEntity;
import com.spring.to_do_app.repositories.UserRepository;
import com.spring.to_do_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<List<ProjectResponseDto>> getAllProjectsOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByEmail(authentication.getName()).get();
        return new ResponseEntity<>(mapToUserProjectDto(user.getProjects()), HttpStatus.OK);
    }

    private List<ProjectResponseDto> mapToUserProjectDto(List<ProjectEntity> projects) {
        return projects.stream().map((project) -> ProjectResponseDto.builder()
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getDone()? constants.COMPLETED: constants.IN_PROGRESS)
                .author(project.getAuthor())
                .build()).collect(Collectors.toList());
    }
}
