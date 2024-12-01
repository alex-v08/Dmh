package com.dmh.UserService.mapper;

import com.dmh.UserService.dto.NewUserResponse;
import com.dmh.UserService.dto.UserDto;
import com.dmh.UserService.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(Users user);

    Users userDtoToUser(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    void updateUserFromDto(UserDto userDto, @MappingTarget Users user);

    @Mapping(source = "id", target = "userId")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "accountId", expression = "java(getAccountIdForUser(user))")
    NewUserResponse userToNewUserResponse(Users user);

    default Long getAccountIdForUser(Users user) {
        // Esta es una implementación temporal.
        // Deberías obtener el accountId real del servicio de cuentas
        // o de donde corresponda según tu arquitectura
        return user.getId(); // Como ejemplo, usamos el mismo ID del usuario
    }
}