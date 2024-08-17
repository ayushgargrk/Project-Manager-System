package com.spring.to_do_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDto {
    @NotNull(message = "first name field is required")
    private String first_name;

    private String last_name;

    @NotNull(message = "email field is required")
    private String email;

    @NotNull(message = "password field is required")
    private String password;

    private String msg;
    private String error;
}
