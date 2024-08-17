package com.spring.to_do_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {
    @NotNull(message = "name field is required")
    private String name;

    @NotNull(message = "description field is required")
    private String description;

    private String author;
    private String error;
}
