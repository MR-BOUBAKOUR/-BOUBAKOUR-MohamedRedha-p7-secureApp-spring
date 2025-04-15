package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.domain.BidList;
import com.PoseidonCapitalSolutions.TradingApp.dto.BidListDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.BidListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping("/bidList")
public class BidListController {

    private final BidListService bidListService;

    @RequestMapping("/list")
    public String home(Model model)
    {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bidList", new BidListDTO());
        return "bidList/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid BidListDTO bidListDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }

        bidListService.create(bidListDTO);
        model.addAttribute("bidLists", bidListService.findAll());
        return "redirect:/bidList/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        BidListDTO bidListDTO = bidListService.findById(id);
        model.addAttribute("bidList", bidListDTO);
        return "bidList/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
                            @Valid BidListDTO bidListDTO,
                            BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.update(id, bidListDTO);
        return "redirect:/bidList/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id")
                            Integer id,
                            Model model) {
        bidListService.delete(id);
        return "redirect:/bidList/list";
    }
}
