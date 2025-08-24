package az.gadzhieva.springwork.product.service;

import az.gadzhieva.springwork.product.dataJPA.UserRepository;
import az.gadzhieva.springwork.product.dto.user.UserRequestDto;
import az.gadzhieva.springwork.product.error.UserNotFoundException;
import az.gadzhieva.springwork.product.mapper.UserMapper;
import az.gadzhieva.springwork.product.security.CustomUserDetails;
import az.gadzhieva.springwork.product.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService  implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

    public void register(UserRequestDto userRequestDto){
        User user = UserMapper.requestToEntity(userRequestDto);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
