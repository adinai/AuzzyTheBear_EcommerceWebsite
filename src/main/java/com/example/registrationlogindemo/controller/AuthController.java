package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("index")
    public String home(){
        return "index";
    }

//    @GetMapping("/login")
//    public String loginForm() {
//        System.out.println("@getmapping(/login) called");
//        return "login";
//    }
    //my pham
    @GetMapping("/login")
    public String loginForm(Model model) {
        System.out.println("@getmapping(/login) called");
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }

    //my pham
    @GetMapping("/success")
    public String getSuccessPage(){
        System.out.println("/success getmapping called");
        return "success";
    }

    @PostMapping("/success")
    public String login(@RequestParam String role) {
        System.out.println("/success postmapping called");
        return "Admin".equals(role) ? "redirect:/users" : "redirect:/test";
    }


    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        System.out.println("calling @getmapping(/users)");
        List<UserDto> users = userService.findAllUsers();
        System.out.println("users" + users);
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/test")
    public String getTestPage(){
        return "test";
    }
}
