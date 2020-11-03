package com.capgemini.jdbc.employee_payroll;

import java.util.Arrays;

public class EmployeePayrollData {
	private int id;
	private String name;
	private double salary;
	private String company_name;
	private String[] department;
	private String date;

	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}
	
	public EmployeePayrollData(int id, String name, double salary, String company_name, String[] department,
			String date) {
		this(id, name, salary);
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.company_name = company_name;
		this.department = department;
		this.date = date;
	}
	
	public double getSalary() {
		return salary;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [id=" + id + ", name=" + name + ", salary=" + salary + ", company_name="
				+ company_name + ", department=" + Arrays.toString(department) + ", date=" + date + "]";
	}
}
