package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.dataJPA.UserRepository;
import az.gadzhieva.springwork.product.dto.user.UserRequestDto;
import az.gadzhieva.springwork.product.error.UserNotFoundException;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.service.MyUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserRepository  userRepository;
    private final MyUserDetailsService myUserDetailsService;

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";  // Имя html-шаблона: login.html
    }


    //  Форма регистрации
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRequestDto());
        return "register"; // имя HTML-шаблона (register.html)
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserRequestDto userRequestDto,
                               BindingResult bindingResult, Model model) {
        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "That username is already taken. Please choose a different one.");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        myUserDetailsService.register(userRequestDto);
        return "redirect:/login";
    }


    @GetMapping("/web/home")
    public String redirectAfterLogin(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return switch (user.getRole()) {
            case "ADMIN" -> "redirect:/admin/home";
            default -> "redirect:/user/home";
        };
    }



}
