package com.spring.to_do_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @NotNull(message = "email field required")
    private String email;

    @NotNull(message = "password field required")
    private String password;

    private String first_name;
    private String last_name;
    private String token;
    private String error;
}
