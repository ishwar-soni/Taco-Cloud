package com.upgrad.tacocloud.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upgrad.tacocloud.entities.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	@GetMapping("/current")
	public String orderForm (Model model) {
		log.info("presenting order form");
		model.addAttribute("order", new Order());
		return "orderForm";
	}
	
	@PostMapping
	public String processOrder (@Valid Order order, Errors errors) {
		if (errors.hasErrors()) {
			return "orderForm";
		}
		
		log.info("processing order details : " + order);
		return "redirect:/";
	}
}
