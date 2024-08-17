package com.spring.to_do_app.repositories;

import com.spring.to_do_app.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Integer> {
    Boolean existsByName(String name);
    Optional<ProjectEntity> findByName(String name);
    List<ProjectEntity> findAllByAuthorEmail(String name);
}
