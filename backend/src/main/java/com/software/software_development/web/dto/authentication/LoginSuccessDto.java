package com.software.software_development.web.dto.authentication;

import com.software.software_development.model.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSuccessDto {
    private String accessToken;
    private String refreshToken;
    private String login;
    private String email;
    private UserRole role;
}
