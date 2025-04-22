package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserUpdateDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "user/add";
    }

    @PostMapping("/validate")
    public String validateUser(@Valid @ModelAttribute("user") UserCreateDTO userCreateDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "user/add";
        }
        userService.create(userCreateDTO);
        return "redirect:/user/list";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        UserResponseDTO userResponseDTO = userService.findById(id);
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();

        userUpdateDTO.setId(userResponseDTO.getId());
        userUpdateDTO.setUsername(userResponseDTO.getUsername());
        userUpdateDTO.setFullname(userResponseDTO.getFullname());
        userUpdateDTO.setRole(userResponseDTO.getRole());
        // Leaving the password empty

        model.addAttribute("user", userUpdateDTO);
        return "user/update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             @Valid @ModelAttribute("user") UserUpdateDTO userUpdateDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userUpdateDTO);
            return "user/update";
        }

        userService.update(id, userUpdateDTO, request, response);
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        userService.delete(id, request, response);
        return "redirect:/user/list";
    }
}
