package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.UserCreateDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserResponseDTO;
import com.PoseidonCapitalSolutions.TradingApp.dto.UserUpdateDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
                             @Valid @ModelAttribute("user") UserUpdateDTO userDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "user/update";
        }

        userService.update(id, userDTO);
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.delete(id);
        return "redirect:/user/list";
    }
}
