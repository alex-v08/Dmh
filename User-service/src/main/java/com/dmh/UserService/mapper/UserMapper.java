package com.dmh.UserService.mapper;

import com.dmh.UserService.dto.UserDto;
import com.dmh.UserService.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(Users user);

    Users userDtoToUser(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget Users user);

}
