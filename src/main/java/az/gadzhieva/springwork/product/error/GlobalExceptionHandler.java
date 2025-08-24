package az.gadzhieva.springwork.product.error;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public String productNotFoundHandler(ProductNotFoundException ex, Model model){
       model.addAttribute("message",ex.getMessage());
       return "exception";
    }

    @ExceptionHandler(FirmNotFoundException.class)
    public String firmNotFoundHandler(FirmNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public String employeeNotFoundHandler(EmployeeNotFoundException ex, Model model){
        model.addAttribute("message",ex.getMessage());
        return "exception";
    }
    @ExceptionHandler(InvoiceProductNotFoundException.class)
    public String invoiceItemNotFoundHandler(InvoiceProductNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public String invoiceNotFoundHandler(InvoiceNotFoundException ex,Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }
    @ExceptionHandler(FirmCanNotDelete.class)
    public String firmCanNotDeletedHandler(FirmCanNotDelete ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundHandler(UserNotFoundException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }
    @ExceptionHandler(CustomAccessDeniedException.class)
    public String accessDeniedHandler(CustomAccessDeniedException ex, Model model){
        model.addAttribute("message", ex.getMessage());
        return "exception";
    }
}
