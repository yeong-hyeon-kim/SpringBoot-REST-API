package com.silica.info.repository;

import java.util.List;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.silica.info.model.Users;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepository {
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final UserRowMapper userRowMapper;
	
	public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.userRowMapper = new UserRowMapper();
	}
	
	public List<Users> findList(){
		return namedParameterJdbcTemplate.query(UserSql.SELECT
					, EmptySqlParameterSource.INSTANCE
					, this.userRowMapper);
	}
	
	public List<Users> findIdentityUser(String id){
		String qry = UserSql.SELECT + UserSql.SELECT_CONDITION;
		SqlParameterSource param = new MapSqlParameterSource("id", id);
		
		return namedParameterJdbcTemplate.query(qry, param, this.userRowMapper);
	}
	
	public Users Insert(Users user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", user.getId())
				.addValue("email", user.getEmail())
				.addValue("name", user.getName())
				.addValue("address", user.getAddress()); 
		int affectedRows = namedParameterJdbcTemplate.update(UserSql.INSERT, parameterSource, keyHolder);
		return user;
	}
	
	public Integer Update(Users user) {
		String qry = UserSql.UPDATE + UserSql.UPDATE_CONDITION;
		
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", user.getId())
				.addValue("name", user.getName())
				.addValue("email", user.getEmail())
				.addValue("address", user.getAddress());
		return namedParameterJdbcTemplate.update(qry, parameterSource);
	}
	
	public Integer Delete(Integer id) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", id); 
		return namedParameterJdbcTemplate.update(UserSql.DELETE + UserSql.DELETE_CONDITION, parameterSource);
	}
}