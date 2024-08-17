package com.spring.to_do_app.security;

import com.spring.to_do_app.constants.constants;
import com.spring.to_do_app.entities.RoleEntity;
import com.spring.to_do_app.entities.UserEntity;
import com.spring.to_do_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(constants.USER_NOT_FOUND));

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(mapToGrandedAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapToGrandedAuthorities(List<RoleEntity> roles) {
        return roles.stream().map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
