package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.UserRepository;
import az.gadzhieva.springwork.product.dto.user.UserResponseDto;
import az.gadzhieva.springwork.product.error.UserNotFoundException;
import az.gadzhieva.springwork.product.mapper.UserMapper;
import az.gadzhieva.springwork.product.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDto> getAllUser(){
        List<User> users = userRepository.findAll();
        List<UserResponseDto> response = users.stream().
                map(UserMapper::entityToResponse).
                collect(Collectors.toList());
        return response;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }


}
