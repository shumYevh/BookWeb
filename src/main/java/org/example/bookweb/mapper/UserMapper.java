package org.example.bookweb.mapper;

import org.example.bookweb.config.MapperConfig;
import org.example.bookweb.dto.user.UserRegistrationRequestDto;
import org.example.bookweb.dto.user.UserResponseDto;
import org.example.bookweb.models.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
