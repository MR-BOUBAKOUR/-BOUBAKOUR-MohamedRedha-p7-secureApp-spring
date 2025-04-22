package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.RuleNameDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.RuleNameService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * The type Rule name controller.
 */
@AllArgsConstructor
@Controller
@RequestMapping("/ruleName")
public class RuleNameController {

    private final RuleNameService ruleNameService;

    /**
     * Home string.
     *
     * @param model the model
     * @return the string
     */
    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "ruleName/list";
    }

    /**
     * Add rule form string.
     *
     * @param model the model
     * @return the string
     */
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleNameDTO());
        return "ruleName/add";
    }

    /**
     * Validate string.
     *
     * @param ruleNameDTO the rule name dto
     * @param result      the result
     * @param model       the model
     * @return the string
     */
    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }

        ruleNameService.create(ruleNameDTO);
        model.addAttribute("ruleNames", ruleNameService.findAll());
        return "redirect:/ruleName/list";
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
        RuleNameDTO ruleNameDTO = ruleNameService.findById(id);
        model.addAttribute("ruleName", ruleNameDTO);
        return "ruleName/update";
    }

    /**
     * Update rule name string.
     *
     * @param id          the id
     * @param ruleNameDTO the rule name dto
     * @param result      the result
     * @param model       the model
     * @return the string
     */
    @PostMapping("/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("ruleName") RuleNameDTO ruleNameDTO,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }

        ruleNameService.update(id, ruleNameDTO);
        return "redirect:/ruleName/list";
    }

    /**
     * Delete rule name string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id,
                                 Model model) {
        ruleNameService.delete(id);
        return "redirect:/ruleName/list";
    }
}
