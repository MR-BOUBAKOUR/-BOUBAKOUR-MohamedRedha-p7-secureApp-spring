package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.BidListService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * The type Bid list controller.
 */
@AllArgsConstructor
@Controller
@RequestMapping("/bidList")
public class BidListController {

    private final BidListService bidListService;

    /**
     * Home string.
     *
     * @param model the model
     * @return the string
     */
    @RequestMapping("/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    /**
     * Add bid form string.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidList", new BidListDTO());
        return "bidList/add";
    }

    /**
     * Validate string.
     *
     * @param bidListDTO the bid list dto
     * @param result     the result
     * @param model      the model
     * @return the string
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bidList") BidListDTO bidListDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }

        bidListService.create(bidListDTO);
        model.addAttribute("bidLists", bidListService.findAll());
        return "redirect:/bidList/list";
    }

    /**
     * Show update form string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        BidListDTO bidListDTO = bidListService.findById(id);
        model.addAttribute("bidList", bidListDTO);
        return "bidList/update";
    }

    /**
     * Update bid string.
     *
     * @param id         the id
     * @param bidListDTO the bid list dto
     * @param result     the result
     * @param model      the model
     * @return the string
     */
    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid BidListDTO bidListDTO,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bidList", bidListDTO);
            return "bidList/update";
        }
        bidListService.update(id, bidListDTO);
        return "redirect:/bidList/list";
    }

    /**
     * Delete bid string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id")
                            Integer id,
                            Model model) {
        bidListService.delete(id);
        return "redirect:/bidList/list";
    }
}
