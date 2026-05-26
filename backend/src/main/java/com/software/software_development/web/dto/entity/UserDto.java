package com.software.software_development.web.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.software.software_development.core.configuration.Constants;
import com.software.software_development.model.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_PATTERN, message = "Login must be 3-30 alphanumeric characters")
    private String login;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = "Password must be 6-60 characters")
    private String password;

    @NotNull
    private UserRole role;

    @Min(1)
    private Long employeeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String employeeName;
}
