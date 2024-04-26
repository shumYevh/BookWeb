package org.example.bookweb.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.exeption.RegistrationException;
import org.example.bookweb.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
