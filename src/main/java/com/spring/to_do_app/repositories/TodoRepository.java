package com.spring.to_do_app.repositories;

import com.spring.to_do_app.entities.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity,Integer> {
    boolean existsByName(String name);
}
