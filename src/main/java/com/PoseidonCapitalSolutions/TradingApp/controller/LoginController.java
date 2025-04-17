package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.repositorie.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/bidList/list";
        }
        return "login";
    }

}
