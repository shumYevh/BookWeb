package org.example.bookweb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.user.UserLoginRequestDto;
import org.example.bookweb.dto.user.UserLoginResponseDto;
import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.exeption.RegistrationException;
import org.example.bookweb.security.AuthenticationService;
import org.example.bookweb.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth management", description = "Endpoint for registration")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register new user",
            description = "Register new user"
    )
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "Login exists user",
            description = "Login exists user and get JWT token"
    )
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto loginRequestDto) {
        return authenticationService.authenticate(loginRequestDto);
    }
}
