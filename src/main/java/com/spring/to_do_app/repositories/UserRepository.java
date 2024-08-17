package com.spring.to_do_app.repositories;

import com.spring.to_do_app.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
