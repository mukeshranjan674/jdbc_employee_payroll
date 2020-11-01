package com.capgemini.jdbc.employee_payroll;

public class DBExceception extends Exception {

	public enum Type {
		WRONG_FILE
	}

	public Type type;

	public DBExceception(String message, Type type) {
		super(message);
		this.type = type;
	}
}
