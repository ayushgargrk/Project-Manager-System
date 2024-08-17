package com.spring.to_do_app.service;

import com.spring.to_do_app.dto.LoginDto;
import com.spring.to_do_app.dto.RegisterDto;
import com.spring.to_do_app.dto.VerificationDto;
import org.springframework.http.ResponseEntity;

public interface ApplicationService {
    ResponseEntity<RegisterDto> registerTheUser(RegisterDto registerDto);
    ResponseEntity<VerificationDto> verifyTheUser(String token, String email);
    ResponseEntity<LoginDto> loginTheUser(LoginDto loginDto);
}
