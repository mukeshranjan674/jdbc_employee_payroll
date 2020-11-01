package com.capgemini.jdbc.employee_payroll;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollServiceDB {
	private static EmployeePayrollServiceDB employeePayrollServiceDB;
	private PreparedStatement employeePayrollDataStatement;

	private EmployeePayrollServiceDB() {
	}

	public static EmployeePayrollServiceDB getInstance() {
		if (employeePayrollServiceDB == null)
			employeePayrollServiceDB = new EmployeePayrollServiceDB();
		return employeePayrollServiceDB;
	}

	public List<EmployeePayrollData> readData() throws SQLException {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
		String sql = "select a.emp_id, a.name, b.net_pay from employee a, payroll b where a.emp_id = b.emp_id ";
		Connection connection = new EmployeePayrollDB().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			int id = resultSet.getInt("emp_id");
			String name = resultSet.getString("name");
			double salary = resultSet.getDouble("net_pay");
			EmployeePayrollData data = new EmployeePayrollData(id, name, salary);
			employeePayrollList.add(data);
		}
		return employeePayrollList;
	}

	public List<EmployeePayrollData> readDataThroughPreparedStatement(String name) throws SQLException {
		List<EmployeePayrollData> employeePayrollDatas = new ArrayList<EmployeePayrollData>();
		if (this.employeePayrollDataStatement == null) {
			this.getPreparedStatement(name);
		}
		ResultSet resultSet = employeePayrollDataStatement.executeQuery();
		employeePayrollDatas = this.getEmployeePayrollData(resultSet);
		return employeePayrollDatas;
	}

	public void updateData(String sql) throws SQLException {
		Connection connection = new EmployeePayrollDB().getConnection();
		Statement statement = connection.createStatement();
		statement.execute(sql);
	}

	public void updateDataUsingPrepared(String sql) throws SQLException {
		Connection connection = new EmployeePayrollDB().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.executeUpdate();
	}

	private void getPreparedStatement(String name) {
			String sql = "select a.emp_id, a.name, b.net_pay from employee a, payroll b where a.emp_id = b.emp_id and a.name = '"
					+ name + "'";
			Connection connection = new EmployeePayrollDB().getConnection();
			try {
				employeePayrollDataStatement = connection.prepareStatement(sql);
			} catch (SQLException e) {
			}
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollDatas = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("emp_id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("net_pay");
				EmployeePayrollData data = new EmployeePayrollData(id, name, salary);
				employeePayrollDatas.add(data);
			}
		} catch (SQLException e) {
		}
		return employeePayrollDatas;
	}
}
