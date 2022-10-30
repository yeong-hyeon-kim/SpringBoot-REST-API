package com.silica.info.repository

class UserSql {
	public static final String SELECT = """SELECT * FROM user""";
	public static final String SELECT_CONDITION = """ WHERE id = :id""";
	public static final String INSERT = """INSERT INTO user (id, email, name, address) VALUES(:id, :email, :name, :address);""";
    public static final String UPDATE = """UPDATE user SET email=:email, name=:name, address=:address """;
	public static final String UPDATE_CONDITION = """WHERE id=:id""";
	public static final String DELETE = """DELETE FROM user """;
	public static final String DELETE_CONDITION = """WHERE id=:id""";
	}
