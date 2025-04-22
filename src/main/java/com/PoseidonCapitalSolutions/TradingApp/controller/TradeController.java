package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.TradeDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.TradeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/add")
    public String addTradeForm(Model model) {
        model.addAttribute("trade", new TradeDTO());
        return "trade/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDTO tradeDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }

        tradeService.create(tradeDTO);
        model.addAttribute("trades", tradeService.findAll());
        return "redirect:/trade/list";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id,
                                 Model model) {
        TradeDTO tradeDTO = tradeService.findById(id);
        model.addAttribute("trade", tradeDTO);
        return "trade/update";
    }

    @PostMapping("/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("trade") TradeDTO tradeDTO,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }

        tradeService.update(id, tradeDTO);
        return "redirect:/trade/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id,
                              Model model) {
        tradeService.delete(id);
        return "redirect:/trade/list";
    }
}
