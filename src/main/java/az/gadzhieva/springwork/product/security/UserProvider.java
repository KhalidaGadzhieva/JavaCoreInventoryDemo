package az.gadzhieva.springwork.product.security;

import az.gadzhieva.springwork.product.error.CustomAccessDeniedException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class UserProvider {
    public User getCurrentUser() {
        CustomUserDetails customUserDetails=(CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetails.getUser();
    }
    public boolean isAdmin(User user){
        return user.getRole().equals("ADMIN");
    }
    public void checkIfAdminThenThrow(HttpSession session, String action) {
//        User currentUser = getCurrentUser();
//        if (isAdmin(currentUser)) {
//            String message = "Admin is not allowed to " + action;
//            log.warn("[ACCESS DENIED] {}", message);
//            throw new CustomAccessDeniedException(message);
//        }
        if (Boolean.TRUE.equals(session.getAttribute("impersonating"))) {
            String message = "Impersonating user is not allowed to " + action;
            log.warn("[ACCESS DENIED - IMPERSONATION] {}", message);
            throw new CustomAccessDeniedException(message);
        }
    }
}
