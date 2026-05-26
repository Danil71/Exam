package com.software.software_development.web.dto.authentication;

import com.software.software_development.core.configuration.Constants;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_PATTERN, message = "Login must be 3-30 alphanumeric characters")
    private String login;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = "Password must be 6-60 characters")
    private String password;
}
