package com.spring.to_do_app.controller;

import com.spring.to_do_app.dto.LoginDto;
import com.spring.to_do_app.dto.RegisterDto;
import com.spring.to_do_app.dto.VerificationDto;
import com.spring.to_do_app.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service")
public class ServiceController {
    private ApplicationService applicationService;

    @Autowired
    public ServiceController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @PostMapping("register")
    public ResponseEntity<RegisterDto> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        return applicationService.registerTheUser(registerDto);
    }

    @PatchMapping("verification/{email}/{token}")
    public ResponseEntity<VerificationDto> verifyUser(@PathVariable("token") String token, @PathVariable("email") String email){
        return applicationService.verifyTheUser(token,email);
    }

    @PostMapping("login")
    public ResponseEntity<LoginDto> loginUser(@Valid @RequestBody LoginDto loginDto){
        return applicationService.loginTheUser(loginDto);
    }
}
