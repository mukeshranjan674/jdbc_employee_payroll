package com.capgemini.jdbc.employee_payroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * UC2
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<EmployeePayrollData> readData() throws SQLException {
		String sql = "select a.emp_id, a.name, b.net_pay from employee a, payroll b where a.emp_id = b.emp_id";
		Connection connection = new EmployeePayrollDB().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		return this.getEmployeePayrollData(resultSet);
	}

	/**
	 * UC3
	 * 
	 * @param sql
	 * @throws SQLException
	 */
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

	/**
	 * UC4
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public List<EmployeePayrollData> readDataThroughPreparedStatement(String name) throws SQLException {
		List<EmployeePayrollData> employeePayrollDatas = new ArrayList<EmployeePayrollData>();
		if (this.employeePayrollDataStatement == null) {
			this.getPreparedStatement(name);
		}
		ResultSet resultSet = employeePayrollDataStatement.executeQuery();
		employeePayrollDatas = this.getEmployeePayrollData(resultSet);
		return employeePayrollDatas;
	}

	/**
	 * UC5
	 * 
	 * @param date
	 * @return
	 * @throws SQLException
	 */
	public List<EmployeePayrollData> readData(Date date) throws SQLException {
		String sql = "SELECT a.emp_id, a.name, b.net_pay from employee a, payroll b "
				+ "where date_of_joining between date('" + date + "') and date(now()) and a.emp_id = b.emp_id";
		Connection connection = new EmployeePayrollDB().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		return this.getEmployeePayrollData(resultSet);
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

	/**
	 * UC6
	 * 
	 * @throws SQLException
	 */
	public Map<String, Map<Character, Double>> getDetails() throws SQLException {
		Map<String, Map<Character, Double>> result = new HashMap<String, Map<Character, Double>>();
		String sql_sum = "select a.gender, sum(b.net_pay) from employee a, payroll b "
				+ "where a.emp_id = b.emp_id group by gender";
		result.put("sum", getMap(sql_sum));
		String sql_avg = "select a.gender, avg(b.net_pay) from employee a, payroll b"
				+ " where a.emp_id = b.emp_id group by gender";
		result.put("avg", getMap(sql_avg));
		String sql_min = "select a.gender, min(b.net_pay) from employee a, payroll b"
				+ " where a.emp_id = b.emp_id group by gender";
		result.put("min", getMap(sql_min));
		String sql_max = "select a.gender, max(b.net_pay) from employee a, payroll b"
				+ " where a.emp_id = b.emp_id group by gender";
		result.put("max", getMap(sql_max));
		String sql_count = "select gender, count(gender) from employee group by gender";
		result.put("count", getMap(sql_count));
		return result;
	}

	public Map<Character, Double> getMap(String sql) throws SQLException {
		Map<Character, Double> map = new HashMap<>();
		Connection connection = new EmployeePayrollDB().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			char charcater = resultSet.getString(1).charAt(0);
			double value = resultSet.getDouble(2);
			map.put(charcater, value);
		}
		return map;
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
	 */
	public EmployeePayrollData addNewEmployee(int id, String name, char gender, String phone_no, String address,
			Date date, double salary) {
		int employeeId = 0;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format("insert into employee values (%s,%s,'%s','%s','%s','%s',date(now()))", 1, id, name,
				gender, phone_no, address);
		try (Connection connection = new EmployeePayrollDB().getConnection()) {
			Statement statement_employee = connection.createStatement();
			int rowAffected = statement_employee.executeUpdate(sql, statement_employee.RETURN_GENERATED_KEYS);

		} catch (SQLException e) {
		}

		double deductions = salary * 0.2;
		double taxable_pay = salary - deductions;
		double tax = taxable_pay * 0.1;
		double net_pay = taxable_pay - tax;
		String sql_salary = String.format("insert into payroll values (%s,%s,%s,%s,%s,%s)", id, salary, deductions,
				taxable_pay, tax, net_pay);
		try (Connection connection = new EmployeePayrollDB().getConnection()) {
			Statement statement_salary = connection.createStatement();
			int rowAffected = statement_salary.executeUpdate(sql_salary);
			if (rowAffected == 1) {
				ResultSet resultSet = statement_salary.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(2);
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary);
			}
		} catch (SQLException e) {
		}
		return employeePayrollData;
	}
}
