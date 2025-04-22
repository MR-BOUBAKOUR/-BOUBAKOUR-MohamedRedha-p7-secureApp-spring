package com.PoseidonCapitalSolutions.TradingApp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * The type Custom error controller.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handle error string.
     *
     * @return the string
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}