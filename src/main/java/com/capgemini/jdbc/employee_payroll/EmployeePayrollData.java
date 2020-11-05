package com.capgemini.jdbc.employee_payroll;

import java.sql.Date;
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
	private int[] dept_id;

	public EmployeePayrollData(int id, String name, double salary) {
		this.emp_id = id;
		this.name = name;
		this.salary = salary;
	}

	public EmployeePayrollData(int id, int comp_id, String name, String gender, String address, String phone_no,
			double salary, String date) {
		this.emp_id = id;
		this.comp_id = comp_id;
		this.name = name;
		this.gender = gender;
		this.address = address;
		this.phone_no = phone_no;
		this.salary = salary;
		this.start_date = date;
	}

	public EmployeePayrollData(int id, String name, char gender, String address, String phone_no, double salary,
			Date date, String company_name, int comp_id, String[] department, int[] dept_id) {
		this(id, comp_id, name, Character.toString(gender), address, phone_no, salary, date.toString());
		this.company_name = company_name;
		this.department = department;
		this.dept_id = dept_id;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public int getComp_id() {
		return comp_id;
	}

	public void setComp_id(int comp_id) {
		this.comp_id = comp_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String[] getDepartment() {
		return department;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public int[] getDept_id() {
		return dept_id;
	}

	public void setDept_id(int[] dept_id) {
		this.dept_id = dept_id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + comp_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		if (comp_id != other.comp_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [emp_id=" + emp_id + ", comp_id=" + comp_id + ", name=" + name + ", gender="
				+ gender + ", address=" + address + ", phone_no=" + phone_no + ", salary=" + salary + ", company_name="
				+ company_name + ", department=" + Arrays.toString(department) + ", start_date=" + start_date + "]";
	}
}
