package com.capgemini.jdbc.employee_payroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.capgemini.jdbc.employee_payroll.EmployeePayrollService.IOService;

public class EmployeePayrollDatabaseTest {

	/**
	 * UC2
	 */
	@Test
	public void whenReadFromDataBaseShouldReturnTheExactSalaries() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(3, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}

	/**
	 * UC3
	 */
	@Test
	public void whenExcecutedDataShouldGetUpdatedInDatabase() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollService.updateEmployeePayrollData("Sita", 3000000);
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(3000000, employeePayrollDatas.get(2).getSalary(), 0.0);
		} catch (DBException e) {
		}
	}

	/**
	 * UC4
	 */
	@Test
	public void whenExcecutedDataShouldGetUpdatedInDatabaseThroughPreparedStatement() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollService.updateDataUsingPrepared("Ram", 250000);
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(250000, employeePayrollDatas.get(0).getSalary(), 0.0);
		} catch (DBException e) {
		}
	}

	@Test
	public void givenNameShouldReturnAllTheRecords() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData("Ram");
			assertEquals(1, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}

	/**
	 * UC5
	 */
	@Test
	public void givenDateShouldReturnAllTheRecords() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			Date date = Date.valueOf("2017-11-10");
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(date);
			assertEquals(3, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}
	
	/**
	 * UC6
	 */
	@Test
	public void givenDbWhenPerformedArithmeticFunctionsShouldPerformTheSame() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Map<Character, Double>> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.getDetails();
			boolean result = employeePayrollDatas.get("sum").get('F') == (3000000)&&
							 employeePayrollDatas.get("sum").get('M') == (310000)&&
							 employeePayrollDatas.get("avg").get('F') == (3000000)&&
							 employeePayrollDatas.get("avg").get('M') == (155000)&&
							 employeePayrollDatas.get("min").get('F') == (3000000)&&
							 employeePayrollDatas.get("min").get('M') == (60000)&&
							 employeePayrollDatas.get("max").get('F') == (3000000)&&
							 employeePayrollDatas.get("max").get('M') == (250000)&&
							 employeePayrollDatas.get("count").get('F') == (1)&&
							 employeePayrollDatas.get("count").get('M') == (2);
			assertTrue(result);
		} catch (DBException e) {
		}
	}
}
