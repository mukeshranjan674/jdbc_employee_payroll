package com.capgemini.jdbc.employee_payroll;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.capgemini.jdbc.employee_payroll.DBException.Type;
import com.capgemini.jdbc.employee_payroll.EmployeePayrollService.IOService;

public class EmployeePayrollService {
	private List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();

	public enum IOService {
		DB_IO
	};

	/**
	 * UC2
	 * 
	 * @param dBService
	 * @return
	 * @throws DBException
	 */
	public List<EmployeePayrollData> readEmployeePayrollData(IOService dBService) throws DBException {
		EmployeePayrollServiceDB.getInstance().loadData();
		if (dBService.equals(IOService.DB_IO)) {
			try {
				this.employeePayrollList = EmployeePayrollServiceDB.getInstance().readData();
			} catch (SQLException e) {
				throw new DBException("Unable to read", Type.WRONG_DATA);
			}
		}
		return employeePayrollList;
	}

	/**
	 * UC4
	 * 
	 * @param name
	 * @return
	 * @throws DBException
	 */
	public List<EmployeePayrollData> readEmployeePayrollData(String name) throws DBException {
		try {
			return EmployeePayrollServiceDB.getInstance().readDataThroughPreparedStatement(name);
		} catch (SQLException e) {
			throw new DBException("Unable to read", Type.WRONG_QUERY);
		}
	}

	/**
	 * UC3
	 * 
	 * @param name
	 * @param salary
	 * @throws DBException
	 */
	public void updateEmployeePayrollData(String name, double salary) throws DBException {
		String sql = "update payroll set net_pay = " + salary
				+ " where emp_id = (select emp_id from employee where name = '" + name + "')";
		this.updateEmployeePayrollData(sql);
	}

	private void updateEmployeePayrollData(String sql) throws DBException {
		try {
			EmployeePayrollServiceDB.getInstance().updateData(sql);
		} catch (SQLException e) {
			throw new DBException("Unable to read", Type.WRONG_QUERY);
		}
	}

	public void updateDataUsingPrepared(String name, double salary) throws DBException {
		String sql = "update payroll set net_pay = " + salary
				+ " where emp_id = (select emp_id from employee where name = '" + name + "')";
		try {
			EmployeePayrollServiceDB.getInstance().updateDataUsingPrepared(sql);
		} catch (SQLException e) {
			throw new DBException("Unable to read", Type.WRONG_QUERY);
		}
	}

	/**
	 * UC5
	 * 
	 * @param date
	 * @return
	 * @throws DBException
	 */
	public List<EmployeePayrollData> readEmployeePayrollData(Date date) throws DBException {
		try {
			this.employeePayrollList = EmployeePayrollServiceDB.getInstance().readData(date);
		} catch (SQLException e) {
			throw new DBException("Unable to read", Type.WRONG_QUERY);
		}
		return employeePayrollList;
	}

	/**
	 * UC6
	 * 
	 * @return
	 * @throws DBException
	 */
	public Map<String, Map<Character, Double>> getDetails() throws DBException {
		try {
			return EmployeePayrollServiceDB.getInstance().getDetails();
		} catch (SQLException e) {
			throw new DBException("Unable to read", Type.WRONG_DATA);
		}
	}

	/**
	 * UC7 UC8
	 * 
	 * @param id
	 * @param name
	 * @param gender
	 * @param phone_no
	 * @param address
	 * @param date
	 * @param salary
	 * @return
	 * @throws DBException
	 */
	public EmployeePayrollData addNewEmployee(int id, String name, char gender, String phone_no, String address,
			Date date, double salary, String comp_name, int comp_id, String[] department, int[] dept_id)
			throws DBException {
		return EmployeePayrollServiceDB.getInstance().addNewEmployee(id, name, gender, phone_no, address, date, salary,
				comp_name, comp_id, department, dept_id);
	}

	public boolean checkInSyncWithDatabase(String name) throws DBException {
		boolean result = false;
		List<EmployeePayrollData> employeePayrollDatas = this.readEmployeePayrollData(IOService.DB_IO);
		EmployeePayrollData data = employeePayrollDatas.stream().filter(n -> n.getName().equals(name)).findAny()
				.orElse(null);
		if (data != null)
			result = true;
		return result;
	}

	/**
	 * UC12
	 * 
	 * @param name
	 * @throws DBException
	 */
	public void removeEmployee(String name) throws DBException {
		if (!this.checkInSyncWithDatabase(name))
			throw new DBException("employee not found", Type.EMPLOYEE_NOT_FOUND);
		EmployeePayrollServiceDB.getInstance().removeEmployee(name);
	}

	/**
	 * UC1_Thread
	 * 
	 * @param employeeList
	 */
	public void addEmployees(List<EmployeePayrollData> employeeList) {
		for (EmployeePayrollData employee : employeeList) {
			try {
				this.addNewEmployee(employee.getEmp_id(), employee.getName(), employee.getGender().charAt(0),
						employee.getPhone_no(), employee.getAddress(), Date.valueOf(employee.getStart_date()),
						employee.getSalary(), employee.getCompany_name(), employee.getComp_id(),
						employee.getDepartment(), employee.getDept_id());
			} catch (DBException e1) {
				System.out.println(e1.getMessage());
			}
		}
	}

	/**
	 * UC2_Thread
	 * 
	 * @param employeePayrollList
	 */
	public void addEmployeesWihtThreads(List<EmployeePayrollData> employeePayrollList) {
		EmployeePayrollServiceDB.getInstance().addEmployees(employeePayrollList);
	}
}
