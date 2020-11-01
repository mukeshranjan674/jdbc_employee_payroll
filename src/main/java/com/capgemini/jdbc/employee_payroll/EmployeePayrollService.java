package com.capgemini.jdbc.employee_payroll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.capgemini.jdbc.employee_payroll.DBExceception.Type;

public class EmployeePayrollService {

	public enum IOService {
		DB_IO
	};

	private List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dBService) throws DBExceception {
		if (dBService.equals(IOService.DB_IO)) {
			try {
				this.employeePayrollList = new EmployeePayrollServiceDB().readData();
			} catch (SQLException e) {
				throw new DBExceception("Unable to read", Type.WRONG_FILE);
			}
		}
		return employeePayrollList;
	}
}