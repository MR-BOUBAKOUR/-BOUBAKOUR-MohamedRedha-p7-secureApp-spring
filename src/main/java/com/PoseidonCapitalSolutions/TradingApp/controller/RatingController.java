package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.RatingDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.RatingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;

    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new RatingDTO());
        return "rating/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDTO ratingDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }

        ratingService.create(ratingDTO);
        model.addAttribute("ratings", ratingService.findAll());
        return "redirect:/rating/list";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        RatingDTO ratingDTO = ratingService.findById(id);
        model.addAttribute("rating", ratingDTO);
        return "rating/update";
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("rating") RatingDTO ratingDTO,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }

        ratingService.update(id, ratingDTO);
        return "redirect:/rating/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id,
                               Model model) {
        ratingService.delete(id);
        return "redirect:/rating/list";
    }
}