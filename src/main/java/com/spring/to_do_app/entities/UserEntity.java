package com.spring.to_do_app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
@ToString(exclude = {"todos","projects"})
public class UserEntity {
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @Id
    private Integer id;
    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstName;
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            unique = true
    )

    private String email;
    private String password;

    private Boolean isEnabled;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TodoEntity> todos;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<ProjectEntity> projects;
}
