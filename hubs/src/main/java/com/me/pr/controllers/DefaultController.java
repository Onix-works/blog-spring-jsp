package com.me.pr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class DefaultController {
	
	@GetMapping
	public String list() {

		return "redirect:/blogs/";
	}

}
