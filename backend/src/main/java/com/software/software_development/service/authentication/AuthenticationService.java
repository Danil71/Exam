package com.software.software_development.service.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.core.utility.JwtUtils;
import com.software.software_development.model.entity.UserEntity;
import com.software.software_development.repository.UserRepository;
import com.software.software_development.service.entity.UserService;
import com.software.software_development.web.dto.authentication.LoginRequestDto;
import com.software.software_development.web.dto.authentication.LoginSuccessDto;
import com.software.software_development.web.dto.authentication.RegisterRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public LoginSuccessDto register(RegisterRequestDto request) {
        UserEntity user = userService.register(request.getLogin(), request.getEmail(), request.getPassword());
        return generateLoginSuccessDto(user, ClientType.WEB);
    }

    public LoginSuccessDto login(LoginRequestDto request) {
        UserEntity user = userRepository.findByLoginIgnoreCase(request.getLogin())
                .orElseThrow(() -> new BadCredentialsException("Invalid login or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid login or password");
        }
        return generateLoginSuccessDto(user, ClientType.WEB);
    }

    public LoginSuccessDto refreshToken(final String refreshToken, ClientType clientType) {
        if (refreshToken == null) {
            throw new CredentialsExpiredException("Refresh token is missing");
        }
        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new CredentialsExpiredException("Refresh token has expired");
        }
        String login = jwtUtils.extractLogin(refreshToken);
        UserEntity user = userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials for refresh token"));
        refreshTokenService.validateAndGetToken(refreshToken, clientType);
        return generateLoginSuccessDto(user, clientType);
    }

    public void logout(String login, ClientType clientType) {
        UserEntity user = userRepository.findByLoginIgnoreCase(login)
                .orElseThrow(() -> new BadCredentialsException("Invalid login: " + login));
        refreshTokenService.deleteToken(user, clientType);
    }

    private LoginSuccessDto generateLoginSuccessDto(UserEntity user, ClientType clientType) {
        String refreshToken = jwtUtils.generateRefreshToken(user);
        refreshTokenService.replaceToken(user, refreshToken, clientType);

        LoginSuccessDto dto = new LoginSuccessDto();
        dto.setAccessToken(jwtUtils.generateAccessToken(user));
        dto.setRefreshToken(refreshToken);
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setEmployeeId(user.getEmployee() != null ? user.getEmployee().getId() : null);
        return dto;
    }
}
