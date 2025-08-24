package az.gadzhieva.springwork.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/home")
public class HomeViewController {
    @GetMapping
    public String homePage(){
        return "home/home";
    }
    @GetMapping("info")
    public String infoPage(){
        return "pages/info";
    }
}
