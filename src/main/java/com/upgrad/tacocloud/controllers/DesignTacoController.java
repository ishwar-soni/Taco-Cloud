package com.upgrad.tacocloud.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.upgrad.tacocloud.entities.Ingredient;
import com.upgrad.tacocloud.entities.Ingredient.Type;
import com.upgrad.tacocloud.entities.Order;
import com.upgrad.tacocloud.entities.Taco;
import com.upgrad.tacocloud.repositories.IngredientRepository;
import com.upgrad.tacocloud.repositories.TacoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepository;
	private final TacoRepository tacoRepository;
	
	@ModelAttribute(name = "order")
	public Order order () {
		log.info("order model executed");
		return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco (Model model) {
		log.info("taco model executed");
		log.info(model.getAttribute("taco")!=null ? model.getAttribute("taco").toString() : "null");
		return new Taco();
	}
	
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepository,
			TacoRepository tacoRepository) {
		this.ingredientRepository = ingredientRepository;
		this.tacoRepository = tacoRepository;
	}
	
	@GetMapping
	public String showDesignForm (Model model) {
		log.info("Executing /design get request");
		Map<String, List<Ingredient>> ingredientMap = getIngredients();
		model.addAllAttributes(ingredientMap);
		model.addAttribute("taco", new Taco());
		
		return "design";
	}
	
	@PostMapping
	public String processForm (@Valid Taco taco, Errors errors, @ModelAttribute Order order, Model model) {
		if (errors.hasErrors()) {
			Map<String, List<Ingredient>> ingredientMap = getIngredients();
			model.addAllAttributes(ingredientMap);
			return "design";
		}
		log.info("processing design : " + taco);
		Taco savedTaco = tacoRepository.save(taco);
		order.addTacos(savedTaco);
		return "redirect:/orders/current";
	}
	
	private Map<String, List<Ingredient>> getIngredients () {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepository.findAll().forEach(ingredient -> ingredients.add(ingredient));
		
		Type[] types = Ingredient.Type.values();
		Map<String, List<Ingredient>> ingredientMap = new LinkedHashMap<>();
		Arrays.stream(types).forEach(type ->
			ingredientMap.put(type.toString().toLowerCase(), new ArrayList<Ingredient>()));
		ingredients.forEach(ingredient ->
			ingredientMap.get(ingredient.getType().toString().toLowerCase()).add(ingredient));
		return ingredientMap;
	}
}
