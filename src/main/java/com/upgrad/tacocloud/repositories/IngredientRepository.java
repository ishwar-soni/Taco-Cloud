package com.upgrad.tacocloud.repositories;

import com.upgrad.tacocloud.entities.Ingredient;

public interface IngredientRepository {
	Iterable<Ingredient> findAll();
	Ingredient findOne (String id);
	Ingredient save (Ingredient ingredient);
}
