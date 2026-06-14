package com.blogapp.controller;

import com.blogapp.entities.Role;
import com.blogapp.entities.User;
import com.blogapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password, @RequestParam Role role) {
        User user = User.builder().name(name).email(email).password(passwordEncoder.encode(password)).role(role).build();
        userRepository.save(user);
        return "redirect:/login";
    }

}