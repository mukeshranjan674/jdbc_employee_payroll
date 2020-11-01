package com.capgemini.jdbc.employee_payroll;

public class EmployeePayrollData {
	private int id;
	private String name;
	private double salary;
	
	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "id=" + id + ", name=" + name + ", salary=" + salary + "\n" ;
	}
}
