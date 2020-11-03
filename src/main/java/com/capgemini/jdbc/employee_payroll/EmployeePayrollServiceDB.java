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

import com.capgemini.jdbc.employee_payroll.DBException.Type;

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

	/**UC7 UC8 UC9 
	 * @param id
	 * @param name
	 * @param gender
	 * @param phone_no
	 * @param address
	 * @param date
	 * @param salary
	 * @param comp_name
	 * @param comp_id
	 * @param department
	 * @param dept_id
	 * @return
	 * @throws DBException
	 */
	public EmployeePayrollData addNewEmployee(int id, String name, char gender, String phone_no, String address,
			Date date, double salary, String comp_name, int comp_id, String[] department, int[] dept_id) throws DBException {
		int employeeId = 0;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format("insert into employee values (%s,%s,'%s','%s','%s','%s',date(now()))", 1, id, name,
				gender, phone_no, address);
		Connection connection = null;
		try {
			connection = new EmployeePayrollDB().getConnection();
			connection.setAutoCommit(false);
			Statement statement_employee = connection.createStatement();
			int rowAffected = statement_employee.executeUpdate(sql, statement_employee.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DBException("Insertion error", Type.WRONG_QUERY);
			}
			return employeePayrollData;
		}

		double deductions = salary * 0.2;
		double taxable_pay = salary - deductions;
		double tax = taxable_pay * 0.1;
		double net_pay = taxable_pay - tax;
		String sql_salary = String.format("insert into payroll values (%s,%s,%s,%s,%s,%s)", id, salary, deductions,
				taxable_pay, tax, net_pay);
		try {
			Statement statement_salary = connection.createStatement();
			int rowAffected = statement_salary.executeUpdate(sql_salary, statement_salary.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement_salary.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(2);
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DBException("Insertion error", Type.WRONG_QUERY);
			}
		}
		
		try {
			Statement statement = connection.createStatement();
			String sql_company = String.format("insert into company values (%s, '%s')", comp_id, comp_name);
			statement.executeUpdate(sql_company);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement statement = connection.createStatement();
			for(int i = 0 ; i < dept_id.length ; i++) {
				String sql_department = String.format("insert into department values (%s,'%s')", dept_id[i], department[i]);
				statement.executeUpdate(sql_department);
			}
		} catch (SQLException e) {
		}
		
		try {
			Statement statement = connection.createStatement();
			for(int i = 0 ; i < dept_id.length ; i++) {
				String sql_emp_department = String.format("insert into Employee_Department values (%s,%s)", id, dept_id[i]);
				statement.executeUpdate(sql_emp_department);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DBException("Connection not closed", Type.CONNECTION_ERROR);
				}
		}
		return employeePayrollData;
	}
}
