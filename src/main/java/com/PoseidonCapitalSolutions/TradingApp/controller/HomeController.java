package com.PoseidonCapitalSolutions.TradingApp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Home controller.
 */
@Controller
public class HomeController
{
	/**
	 * Home string.
	 *
	 * @param authentication the authentication
	 * @return the string
	 */
	@RequestMapping("/")
	public String home(Authentication authentication)
	{
		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/bidList/list";
		}

		return "home";
	}

	/**
	 * Admin home string.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}

}
