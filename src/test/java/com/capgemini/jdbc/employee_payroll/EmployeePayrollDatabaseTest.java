package com.capgemini.jdbc.employee_payroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.jdbc.employee_payroll.EmployeePayrollService.IOService;

public class EmployeePayrollDatabaseTest {
	EmployeePayrollService employeePayrollService;
	
	@Before
	public void setUp() {
		employeePayrollService = new EmployeePayrollService();
	}

	/**
	 * UC2
	 */
	@Test
	public void whenReadFromDataBaseShouldReturnTheExactSalaries() {
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(4, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}

	/**
	 * UC3
	 */
	@Test
	public void whenExcecutedDataShouldGetUpdatedInDatabase() {
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollService.updateEmployeePayrollData("Sita", 3000000);
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			boolean result = employeePayrollService.checkInSyncWithDatabase("Sita");
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	/**
	 * UC4
	 */
	@Test
	public void whenExcecutedDataShouldGetUpdatedInDatabaseThroughPreparedStatement() {
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollService.updateDataUsingPrepared("Ram", 250000);
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			boolean result = employeePayrollService.checkInSyncWithDatabase("Ram");
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	@Test
	public void givenNameShouldReturnAllTheRecords() {
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
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			Date date = Date.valueOf("2017-11-10");
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(date);
			assertEquals(4, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}
	
	/**
	 * UC6
	 */
	@Test
	public void givenDbWhenPerformedArithmeticFunctionsShouldPerformTheSame() {
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
	
	/**
	 * UC7 UC8
	 */
	@Test
	public void givenEmployeeWhenAddedShouldGetAddedToTheDatabase() {
		;
		Date date = Date.valueOf("2020-11-03");
		EmployeePayrollData employeePayrollData = employeePayrollService.addNewEmployee
												  (1003, "Sita", 'M', "8585656235", "Jharkhand 898985", date, 3000000);
		try {
			boolean result = employeePayrollService.checkInSyncWithDatabase("Laxman");
			assertTrue(result);
		} catch (DBException e) {
		}
	}
}
