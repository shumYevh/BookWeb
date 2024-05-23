package org.example.bookweb.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.exeption.RegistrationException;
import org.example.bookweb.mapper.UserMapper;
import org.example.bookweb.models.Role;
import org.example.bookweb.models.User;
import org.example.bookweb.repository.RoleRepository;
import org.example.bookweb.repository.UserRepository;
import org.example.bookweb.service.shopping.cart.ShoppingCartService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User newUser = userMapper.toModel(requestDto);
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        newUser.setRoles(Set.of(roleRepository.getByRole(Role.RoleName.USER)));
        User savedUser = userRepository.save(newUser);
        shoppingCartService.createShoppingCart(savedUser);
        return userMapper.toUserResponse(savedUser);
    }
}
