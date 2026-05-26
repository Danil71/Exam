package com.software.software_development.web.dto.authentication;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.security.enums.ClientType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDto {

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_PATTERN)
    private String login;

    private ClientType clientType = ClientType.WEB;
}
