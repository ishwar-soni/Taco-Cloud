package com.upgrad.tacocloud.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.upgrad.tacocloud.entities.Ingredient;
import com.upgrad.tacocloud.entities.Ingredient.Type;
import com.upgrad.tacocloud.entities.Taco;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
public class DesignTacoController {
	
	@GetMapping
	public String showDesignForm (Model model) {
		log.info("Executing /design get request");
		List<Ingredient> ingredients = Arrays.asList(
			new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
			new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
			new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
			new Ingredient("CARN", "Carnitas", Type.PROTEIN),
			new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
			new Ingredient("LETC", "Lettuce", Type.VEGGIES),
			new Ingredient("CHED", "Cheddar", Type.CHEESE),
			new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
			new Ingredient("SLSA", "Salsa", Type.SAUCE),
			new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
		);
		
		Type[] types = Ingredient.Type.values();
		Map<String, List<Ingredient>> ingredientMap = new LinkedHashMap<>();
		Arrays.stream(types).forEach(type -> ingredientMap.put(type.toString().toLowerCase(), new ArrayList<Ingredient>()));
		ingredients.forEach(ingredient -> ingredientMap.get(ingredient.getType().toString().toLowerCase()).add(ingredient));
		model.addAllAttributes(ingredientMap);
		model.addAttribute("design", new Taco());
		
		return "design";
	}
	
	@PostMapping
	public String processForm (Taco design) {
		log.info("processing design : " + design);
		return "redirect:/orders/current";
	}
}
