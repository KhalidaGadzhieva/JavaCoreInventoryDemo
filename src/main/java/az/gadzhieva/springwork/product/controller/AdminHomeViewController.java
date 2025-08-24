package az.gadzhieva.springwork.product.controller;

import az.gadzhieva.springwork.product.security.CustomUserDetails;
import az.gadzhieva.springwork.product.security.User;
import az.gadzhieva.springwork.product.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/home")
@RequiredArgsConstructor
public class AdminHomeViewController {

    private final UserService userService;

    @GetMapping
    public String getAllUsers(Model model){
        model.addAttribute("users",userService.getAllUser());
        return "home/users";
    }

    @GetMapping("/impersonate/{userId}")
    public String impersonateUser(@PathVariable Long userId, HttpSession session) {
        User user = userService.findById(userId);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Вот тут сохраняем флаг в сессии
        session.setAttribute("impersonating", true);

        return "redirect:/user/home";
    }




}
