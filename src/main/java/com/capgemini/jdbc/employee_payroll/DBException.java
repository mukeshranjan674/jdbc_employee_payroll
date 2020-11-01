package com.capgemini.jdbc.employee_payroll;

public class DBException extends Exception {

	public enum Type {
		WRONG_DATA, WRONG_QUERY 
	}

	public Type type;

	public DBException(String message, Type type) {
		super(message);
		this.type = type;
	}
}
