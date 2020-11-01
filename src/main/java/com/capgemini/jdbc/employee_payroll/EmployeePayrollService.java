package com.capgemini.jdbc.employee_payroll;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.jdbc.employee_payroll.DBExceception.Type;

public class EmployeePayrollService {
	private List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
	
	public enum IOService {
		DB_IO
	}; 

	public List<EmployeePayrollData> readEmployeePayrollData(IOService dBService) throws DBExceception {
		if (dBService.equals(IOService.DB_IO)) {
			try {
				this.employeePayrollList = EmployeePayrollServiceDB.getInstance().readData();
			} catch (SQLException e) {
				throw new DBExceception("Unable to read", Type.WRONG_DATA);
			}
		}
		return employeePayrollList;
	}
	
	public List<EmployeePayrollData> readEmployeePayrollData(String name) throws DBExceception{
		try {
			return EmployeePayrollServiceDB.getInstance().readDataThroughPreparedStatement(name);
		} catch (SQLException e) {
			throw new DBExceception("Unable to read", Type.WRONG_QUERY);
		}
	}

	public void updateEmployeePayrollData(String name, double salary) throws DBExceception {
		String sql = "update payroll set net_pay = " + salary
				+ " where emp_id = (select emp_id from employee where name = '"+ name + "')";
		this.updateEmployeePayrollData(sql);
	}

	private void updateEmployeePayrollData(String sql) throws DBExceception {
		try {
			EmployeePayrollServiceDB.getInstance().updateData(sql);
		} catch (SQLException e) {
			throw new DBExceception("Unable to read", Type.WRONG_QUERY);
		}
	}
	
	public void updateDateUsingPrepared(String name, double salary) throws DBExceception {
		String sql = "update payroll set net_pay = " + salary
				+ " where emp_id = (select emp_id from employee where name = '"+ name +"')";
		try {
			this.updateDateUsingPrepared(sql);
		} catch (SQLException e) {
			throw new DBExceception("Unable to read", Type.WRONG_QUERY);
		}
	}
	
	private void updateDateUsingPrepared(String sql) throws SQLException {
		EmployeePayrollServiceDB.getInstance().updateDataUsingPrepared(sql);
	}
}
