package com.capgemini.jdbc.employee_payroll;

public class DBExceception extends Exception {

	public enum Type {
		WRONG_DATA, WRONG_QUERY 
	}

	public Type type;

	public DBExceception(String message, Type type) {
		super(message);
		this.type = type;
	}
}
