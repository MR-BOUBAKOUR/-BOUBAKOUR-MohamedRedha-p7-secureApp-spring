package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Login controller.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping
public class LoginController {

    private final UserRepository userRepository;

    /**
     * Login page string.
     *
     * @param authentication the authentication
     * @return the string
     */
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/bidList/list";
        }
        return "login";
    }

}
