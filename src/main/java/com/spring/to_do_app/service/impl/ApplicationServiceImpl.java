package com.spring.to_do_app.service.impl;

import com.spring.to_do_app.constants.Mails;
import com.spring.to_do_app.constants.constants;
import com.spring.to_do_app.dto.LoginDto;
import com.spring.to_do_app.dto.RegisterDto;
import com.spring.to_do_app.dto.VerificationDto;
import com.spring.to_do_app.entities.RoleEntity;
import com.spring.to_do_app.entities.UserEntity;
import com.spring.to_do_app.repositories.RoleRepository;
import com.spring.to_do_app.repositories.UserRepository;
import com.spring.to_do_app.security.JwtService;
import com.spring.to_do_app.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public ApplicationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                  PasswordEncoder passwordEncoder, EmailService emailService, JwtService jwtService,
                                  AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<RegisterDto> registerTheUser(RegisterDto registerDto) {
        Boolean userExist = userRepository.existsByEmail(registerDto.getEmail());
        if(userExist){
            RegisterDto dto = new RegisterDto();
            dto.setError(constants.EMAIL_ALREADY_EXISTS);
            return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
        }

        RoleEntity role = roleRepository.findByName("USER").get();
        UserEntity user = UserEntity.builder()
                .firstName(registerDto.getFirst_name())
                .lastName(registerDto.getLast_name())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .isEnabled(false)
                .roles(List.of(role))
                .build();

        userRepository.save(user);

        final String token = jwtService.generateToken(user.getEmail());
        Mails.Email email = Mails.registerationToken(registerDto.getEmail(),token);

        if(email != null){
            try {
                emailService.sendMail(registerDto.getEmail(),email.getSubject(),email.getBody());
            }catch (Exception e){
            }

        }

        RegisterDto response = RegisterDto.builder()
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .msg(constants.VERIFICATION_TOKEN_SENT)
                .build();

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VerificationDto> verifyTheUser(String token, String email) {
        Boolean userExists = userRepository.existsByEmail(email);
        VerificationDto response = new VerificationDto();

        if (!userExists) {
            response.setError(constants.UNAUTHORIZED);
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }

        String userEmail = jwtService.extractUserEmail(token);
        if(userEmail == null){
            token = jwtService.generateToken(email);
            Mails.Email Email = Mails.registerationToken(email,token);

            if(email != null){
                try{
                    emailService.sendMail(email,Email.getSubject(),Email.getBody());
                }catch (Exception e){

                }

            }
            response.setError(constants.RESEND_VERIFICATION_TOKEN);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

        if(!userEmail.equals(email)){
            response.setError(constants.UNAUTHORIZED);
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }

        UserEntity user = userRepository.findByEmail(email).get();
        user.setIsEnabled(true);
        userRepository.save(user);
        response.setMsg(constants.VERIFIED_NOW);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<LoginDto> loginTheUser(LoginDto loginDto) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = jwtService.generateToken(loginDto.getEmail());

            UserEntity user = userRepository.findByEmail(loginDto.getEmail()).get();
            if(!user.getIsEnabled()){
                Mails.Email email = Mails.registerationToken(loginDto.getEmail(),token);
                if(email != null){
                    try {
                        emailService.sendMail(loginDto.getEmail(), email.getSubject(), email.getBody());
                    }catch (Exception e){

                    }
                }
                LoginDto dto = new LoginDto();
                dto.setError(constants.NOT_VERIFIED + " " + constants.RESEND_VERIFICATION_TOKEN);
                return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
            }

            UserEntity userEntity = userRepository.findByEmail(authentication.getName()).get();
            LoginDto dto = LoginDto.builder()
                    .first_name(userEntity.getFirstName())
                    .last_name(userEntity.getLastName())
                    .email(userEntity.getEmail())
                    .token(token)
                    .build();
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }catch (Exception e){
            LoginDto dto = new LoginDto();
            dto.setError(e.getMessage());
            return new ResponseEntity<>(dto,HttpStatus.UNAUTHORIZED);
        }
    }
}
