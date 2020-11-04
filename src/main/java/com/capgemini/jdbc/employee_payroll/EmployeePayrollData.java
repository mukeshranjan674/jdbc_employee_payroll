package com.capgemini.jdbc.employee_payroll;

import java.util.Arrays;

public class EmployeePayrollData {
	private int emp_id;
	private int comp_id;
	private String name;
	private String gender;
	private String address;
	private String phone_no;
	private double salary;
	private String company_name;
	private String[] department;
	private String start_date;

	public EmployeePayrollData(int id, String name, double salary) {
		this.emp_id = id;
		this.name = name;
		this.salary = salary;
	}
	
	public EmployeePayrollData(int id, String name, double salary, String company_name, String[] department,
			String date) {
		this(id, name, salary);
		this.emp_id = id;
		this.name = name;
		this.salary = salary;
		this.company_name = company_name;
		this.department = department;
		this.start_date = date;
	}
	
	public EmployeePayrollData(int id, int comp_id, String name, String gender, String address, String phone_no, double salary,
			                   String date) {
		this.emp_id = id;
		this.comp_id = comp_id;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.phone_no = phone_no;
		this.salary = salary;
		this.start_date = date;
	}
	
	public double getSalary() {
		return salary;
	}

	public String getName() {
		return name;
	}

	public int getComp_id() {
		return comp_id;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public void setComp_id(int comp_id) {
		this.comp_id = comp_id;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [emp_id=" + emp_id + ", comp_id=" + comp_id + ", name=" + name + ", gender="
				+ gender + ", address=" + address + ", phone_no=" + phone_no + ", salary=" + salary + ", company_name="
				+ company_name + ", department=" + Arrays.toString(department) + ", start_date=" + start_date + "]";
	}
}
