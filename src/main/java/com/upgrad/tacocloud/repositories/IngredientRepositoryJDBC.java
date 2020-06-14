package com.upgrad.tacocloud.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.upgrad.tacocloud.entities.Ingredient;

@Repository
public class IngredientRepositoryJDBC implements IngredientRepository{
	
	private JdbcTemplate jdbcTemplate;
	
	private final RowMapper<Ingredient> ingredientRowMapper = (rs, rowNum) -> {
		return new Ingredient(
					rs.getString("id"),
					rs.getString("name"),
					Ingredient.Type.valueOf(rs.getString("type"))
				);
	};
	
	@Autowired
	public IngredientRepositoryJDBC(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		return jdbcTemplate.query("selct id, name, type from ingredient", ingredientRowMapper);
	}

	@Override
	public Ingredient findOne(String id) {
		return jdbcTemplate.queryForObject("select id, name, type from ingredient where id = ?",
				ingredientRowMapper,
				id);
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbcTemplate.update("insert into ingredient (id, name, type) values (?, ?, ?)",
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType());
		return ingredient;
	}

}
