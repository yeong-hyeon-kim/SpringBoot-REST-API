package com.silica.info.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.silica.info.model.Users;

public class UserRowMapper implements RowMapper<Users> {

	@Override
	public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
		Users user = new Users();
		user.setId(rs.getString("ID"));
		user.setEmail(rs.getString("EMAIL"));
		user.setName(rs.getString("NAME"));
		user.setAddress(rs.getString("ADDRESS"));
		return user;
	}

}

