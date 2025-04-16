package com.PoseidonCapitalSolutions.TradingApp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home(Authentication authentication)
	{
		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/bidList/list";
		}

		return "home";
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}

}
