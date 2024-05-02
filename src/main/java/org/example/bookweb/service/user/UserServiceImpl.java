package org.example.bookweb.service.user;

import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.exeption.RegistrationException;
import org.example.bookweb.mapper.UserMapper;
import org.example.bookweb.models.Role;
import org.example.bookweb.models.User;
import org.example.bookweb.repository.RoleRepository;
import org.example.bookweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public static final Role.RoleName USER_ROLE = Role.RoleName.USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User newUser = new User();
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        newUser.setFirstName(requestDto.getFirstName());
        newUser.setLastName(requestDto.getLastName());
        newUser.setShippingAddress(requestDto.getShippingAddress());
        newUser.setRoles(Set.of(roleRepository.findFirstByRole(USER_ROLE)));
        User savedUser = userRepository.save(newUser);
        return userMapper.toUserResponse(savedUser);
    }
}
