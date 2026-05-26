package com.software.software_development.web.controller.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.software.software_development.core.configuration.Constants;
import com.software.software_development.core.security.enums.ClientType;
import com.software.software_development.service.authentication.AuthenticationService;
import com.software.software_development.web.dto.authentication.LoginRequestDto;
import com.software.software_development.web.dto.authentication.LoginSuccessDto;
import com.software.software_development.web.dto.authentication.LogoutRequestDto;
import com.software.software_development.web.dto.authentication.RefreshTokenRequestDto;
import com.software.software_development.web.dto.authentication.RegisterRequestDto;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(Constants.API_URL + Constants.AUTH_URL)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping(Constants.REGISTER_URL)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LoginSuccessDto> register(@RequestBody @Valid RegisterRequestDto request, HttpServletResponse response) {
        LoginSuccessDto loginSuccess = authenticationService.register(request);
        setRefreshCookie(response, loginSuccess.getRefreshToken());
        loginSuccess.setRefreshToken(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(loginSuccess);
    }

    @PostMapping(Constants.LOGIN_URL)
    public ResponseEntity<LoginSuccessDto> login(@RequestBody @Valid LoginRequestDto request, HttpServletResponse response) {
        LoginSuccessDto loginSuccess = authenticationService.login(request);
        setRefreshCookie(response, loginSuccess.getRefreshToken());
        loginSuccess.setRefreshToken(null);
        return ResponseEntity.ok(loginSuccess);
    }

    @PutMapping("/refresh-token")
    public ResponseEntity<LoginSuccessDto> refreshToken(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        LoginSuccessDto loginSuccess = authenticationService.refreshToken(refreshToken, ClientType.WEB);
        setRefreshCookie(response, loginSuccess.getRefreshToken());
        loginSuccess.setRefreshToken(null);
        return ResponseEntity.ok(loginSuccess);
    }

    @PutMapping("/refresh-token-direct")
    public LoginSuccessDto refreshTokenDirect(@RequestBody @Valid RefreshTokenRequestDto request) {
        return authenticationService.refreshToken(request.getRefreshToken(), ClientType.DESKTOP);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid LogoutRequestDto request, HttpServletResponse response) {
        authenticationService.logout(request.getLogin(), ClientType.WEB);
        clearRefreshCookie(response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-direct")
    public ResponseEntity<Void> logoutDirect(@RequestBody @Valid LogoutRequestDto request) {
        authenticationService.logout(request.getLogin(), request.getClientType());
        return ResponseEntity.noContent().build();
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(Constants.API_URL + Constants.AUTH_URL)
                .maxAge(30 * 24 * 60 * 60)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path(Constants.API_URL + Constants.AUTH_URL)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
