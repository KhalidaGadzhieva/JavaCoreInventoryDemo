package az.gadzhieva.springwork.product.mapper;

import az.gadzhieva.springwork.product.dto.user.UserRequestDto;
import az.gadzhieva.springwork.product.dto.user.UserResponseDto;
import az.gadzhieva.springwork.product.security.User;

public class UserMapper {
    public static User requestToEntity(UserRequestDto userRequestDto){
        return User.builder().
                username(userRequestDto.getUsername()).
                password(userRequestDto.getPassword()).
                email(userRequestDto.getEmail()).
                role("USER").
                build();
    }
    public static UserResponseDto entityToResponse(User user){
        return UserResponseDto.builder().
                id(user.getId()).
                username(user.getUsername()).
                role(user.getRole()).
                email(user.getEmail()).
                build();
    }
}
