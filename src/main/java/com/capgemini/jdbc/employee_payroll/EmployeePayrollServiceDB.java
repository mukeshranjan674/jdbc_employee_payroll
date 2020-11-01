package com.capgemini.jdbc.employee_payroll;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollServiceDB {

	private List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
	
	public List<EmployeePayrollData>  readData() throws SQLException{
		String sql = "select a.emp_id, a.name, b.net_pay from employee a, payroll b where a.emp_id = b.emp_id ";
		Connection connection = new EmployeePayrollDB().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()) {
			int id = resultSet.getInt("emp_id");
			String name = resultSet.getString("name");
			double salary = resultSet.getDouble("net_pay");
			EmployeePayrollData data = new EmployeePayrollData(id, name, salary);
			employeePayrollList.add(data);
		}
		return employeePayrollList;
	}
}
