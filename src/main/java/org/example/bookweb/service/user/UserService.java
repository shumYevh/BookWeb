package org.example.bookweb.service.user;

import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.exeption.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
