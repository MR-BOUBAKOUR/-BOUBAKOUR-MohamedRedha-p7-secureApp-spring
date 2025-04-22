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

/**
 * The type Trade controller.
 */
@AllArgsConstructor
@Controller
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    /**
     * Home string.
     *
     * @param model the model
     * @return the string
     */
    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.findAll());
        return "trade/list";
    }

    /**
     * Add trade form string.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/add")
    public String addTradeForm(Model model) {
        model.addAttribute("trade", new TradeDTO());
        return "trade/add";
    }

    /**
     * Validate string.
     *
     * @param tradeDTO the trade dto
     * @param result   the result
     * @param model    the model
     * @return the string
     */
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
        TradeDTO tradeDTO = tradeService.findById(id);
        model.addAttribute("trade", tradeDTO);
        return "trade/update";
    }

    /**
     * Update trade string.
     *
     * @param id       the id
     * @param tradeDTO the trade dto
     * @param result   the result
     * @param model    the model
     * @return the string
     */
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

    /**
     * Delete trade string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id,
                              Model model) {
        tradeService.delete(id);
        return "redirect:/trade/list";
    }
}
