package com.noodle.noodle.Controller;

import com.noodle.noodle.Models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class AuthenticationController {
    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView, User user) {
        modelAndView.setViewName("base-layout");
        modelAndView.addObject("view", "views/index");
        return modelAndView;
    }
    @GetMapping("/logged")
    public String logged(Authentication authentication){
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        if(hasUserRole) {
            return "redirect:/admin/logged";
        }
        return "redirect:/";
    }
}
