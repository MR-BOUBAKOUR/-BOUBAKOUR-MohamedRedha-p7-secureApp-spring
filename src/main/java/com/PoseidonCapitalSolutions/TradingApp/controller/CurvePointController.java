package com.PoseidonCapitalSolutions.TradingApp.controller;

import com.PoseidonCapitalSolutions.TradingApp.dto.CurvePointDTO;
import com.PoseidonCapitalSolutions.TradingApp.service.CurvePointService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/curvePoint")
public class CurvePointController {

    private final CurvePointService curvePointService;

    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/add")
    public String addCurveForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointDTO());
        return "curvePoint/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }

        curvePointService.create(curvePointDTO);
        model.addAttribute("curvePoints", curvePointService.findAll());
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePointDTO curvePointDTO = curvePointService.findById(id);
        model.addAttribute("curvePoint", curvePointDTO);
        return "curvePoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id,
                              @Valid @ModelAttribute("curvePoint") CurvePointDTO curvePointDTO,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }

        curvePointService.update(id, curvePointDTO);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id, Model model) {
        curvePointService.delete(id);
        return "redirect:/curvePoint/list";
    }
}
