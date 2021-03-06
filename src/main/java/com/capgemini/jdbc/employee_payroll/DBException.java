package com.capgemini.jdbc.employee_payroll;

public class DBException extends Exception {

	public enum Type {
		WRONG_DATA, WRONG_QUERY, CONNECTION_ERROR, EMPLOYEE_NOT_FOUND
	}

	public Type type;

	public DBException(String message, Type type) {
		super(message);
		this.type = type;
	}
}
