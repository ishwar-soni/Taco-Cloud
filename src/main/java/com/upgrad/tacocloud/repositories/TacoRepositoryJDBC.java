package com.upgrad.tacocloud.repositories;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.upgrad.tacocloud.entities.Taco;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TacoRepositoryJDBC implements TacoRepository{

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public TacoRepositoryJDBC(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for (String ingredientId: taco.getIngredients()) {
			saveIngredientToTaco(
					ingredientId,
					tacoId
				);
		}
		return taco;
	}
	
	private long saveTacoInfo (Taco taco) {
		taco.setCreatedAt(new Date());
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
					"insert into taco (name, createdAt) values (?, ?)",
					Types.VARCHAR, Types.DATE
				);
		
		pscf.setReturnGeneratedKeys(true);
		
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
						Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime()))
					);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, keyHolder);
		log.info("" + keyHolder.getKey());
		return keyHolder.getKey().longValue();
	}
	
	private void saveIngredientToTaco (String ingredientId, long tacoId) {
		jdbcTemplate.update("insert into taco_ingredients (taco, ingredient) values (?, ?)",
				tacoId,
				ingredientId);
	}

}
