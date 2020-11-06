package com.capgemini.jdbc.employee_payroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
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
		try {
			employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		} catch (DBException e) {
		}
	}

	/**
	 * UC2
	 */
	@Test
	public void whenReadFromDataBaseShouldReturnTheExactSalaries() {
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);

			assertEquals(2, employeePayrollDatas.size());
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
			employeePayrollService.updateDataUsingPrepared("Sita", 250000);
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			boolean result = employeePayrollService.checkInSyncWithDatabase("Sita");
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	@Test
	public void givenNameShouldReturnAllTheRecords() {
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData("Sita");
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
			assertEquals(2, employeePayrollDatas.size());
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
			boolean result = employeePayrollDatas.get("sum").get('F') == (5880000)
					&& employeePayrollDatas.get("sum").get('M') == (3744000)
					&& employeePayrollDatas.get("avg").get('F') == (2940000)
					&& employeePayrollDatas.get("avg").get('M') == (3744000)
					&& employeePayrollDatas.get("min").get('F') == (2880000)
					&& employeePayrollDatas.get("min").get('M') == (3744000)
					&& employeePayrollDatas.get("max").get('F') == (3000000)
					&& employeePayrollDatas.get("max").get('M') == (3744000)
					&& employeePayrollDatas.get("count").get('F') == (2)
					&& employeePayrollDatas.get("count").get('M') == (1);
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	/**
	 * UC7 UC8 UC9
	 */
	@Test
	public void givenEmployeeWhenAddedShouldGetAddedToTheDatabase() {
		Date date = Date.valueOf("2020-11-03");
		boolean result;
		String[] departments = { "Sales", "Marketing" };
		int[] dept_id = { 01, 02 };
		try {
			EmployeePayrollData employeePayrollData = employeePayrollService.addNewEmployee(1003, "Sita", 'F',
					"8585656235", "Jharkhand 898985", date, 3000000, "Capgemini", 111, departments, dept_id);
			result = employeePayrollService.checkInSyncWithDatabase("Sita");
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	/**
	 * UC11
	 */
	@Test
	public void givenNewEmployeeWhenAddedShouldGetAddedToTheDatabase() {
		Date date = Date.valueOf("2020-11-03");
		boolean result;
		String[] departments = { "Sales", };
		int[] dept_id = { 01 };
		try {
			EmployeePayrollData employeePayrollData = employeePayrollService.addNewEmployee(1003, "Ram", 'M',
					"8585656235", "Jharkhand 898985", date, 5200000, "Microsoft", 112, departments, dept_id);
			result = employeePayrollService.checkInSyncWithDatabase("Lata");
			assertTrue(result);
		} catch (DBException e) {
		}
	}

	/**
	 * UC12
	 */
	@Test
	public void givenNameWhenDeletedShouldGetDeletedFromDatabase() {
		try {
			employeePayrollService.removeEmployee("Ram");
			boolean result = employeePayrollService.checkInSyncWithDatabase("Ram");
			assertFalse(result);
		} catch (DBException e) {
		}
	}

	/**
	 * Array Of Employees
	 * 
	 * @return
	 */
	public EmployeePayrollData[] getArrayOfEmployees() {
		Date[] dates = { Date.valueOf("2020-01-01"), Date.valueOf("2019-12-01"), Date.valueOf("2019-08-31") };
		String[] department_1 = { "Sales" };
		String[] department_2 = { "Sales", "Marketing" };
		String[] department_3 = { "Sales", "Marketing", "Accounts" };
		int[] dept_id_1 = { 1 };
		int[] dept_id_2 = { 1, 2 };
		int[] dept_id_3 = { 1, 2, 3 };
		EmployeePayrollData[] arrOfEmp = {
				new EmployeePayrollData(1004, "Rohan", 'M', "Jharkhand 898985", "8585656235", 5200000.0, dates[0],
						"Microsoft", 112, department_1, dept_id_1),
				new EmployeePayrollData(1005, "Mohan", 'M', "Dhanbad 898989", "98778585458", 2100000.0, dates[1],
						"Capgemini", 111, department_2, dept_id_2),
				new EmployeePayrollData(1006, "Chandan", 'M', "Dhanbad 898989", "98778585458", 100000.0, dates[2],
						"Capgemini", 112, department_3, dept_id_3) };
		return arrOfEmp;
	}

	/**
	 * UC1_Thread
	 */
	@Test
	public void given3EmployeesShouldGetAddedToTheDatabase() {
		EmployeePayrollData[] arrOfEmp = this.getArrayOfEmployees();
		Instant start = Instant.now();
		employeePayrollService.addEmployees(Arrays.asList(arrOfEmp));
		Instant end = Instant.now();
		System.out.println("Duration without thread : " + Duration.between(start, end));
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(5, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}

	/**
	 * UC2_Thread UC3_Thread
	 */
	@Test
	public void given3EmployeesShouldGetAddedToTheDatabaseWihtThreads() {
		EmployeePayrollData[] arrOfEmp = this.getArrayOfEmployees();
		Instant start_thread = Instant.now();
		employeePayrollService.addEmployeesWihtThreads(Arrays.asList(arrOfEmp));
		Instant end_thread = Instant.now();
		System.out.println("Duration with thread : " + Duration.between(start_thread, end_thread));
		List<EmployeePayrollData> employeePayrollDatas;
		try {
			employeePayrollDatas = employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
			assertEquals(5, employeePayrollDatas.size());
		} catch (DBException e) {
		}
	}

}
