package com.capgemini.jdbc.employee_payroll;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.capgemini.jdbc.employee_payroll.EmployeePayrollService.IOService;

public class EmployeePayrollDatabaseTest {
	
	@Test
	public void whenReadFromDataBaseShouldReturnTheExactSalaries() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(3, employeePayrollDatas.size());
		} catch (DBExceception e) {
		}

	}
}
