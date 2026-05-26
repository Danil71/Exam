package com.software.software_development.web.dto.authentication;

import com.software.software_development.core.configuration.Constants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_PATTERN, message = "Invalid login format")
    private String login;

    @NotBlank
    @Pattern(regexp = Constants.PASSWORD_PATTERN, message = "Password must be 6-60 characters")
    private String password;
}
