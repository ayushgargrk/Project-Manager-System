package com.spring.to_do_app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationDto {
    private String msg;
    private String error;
}
