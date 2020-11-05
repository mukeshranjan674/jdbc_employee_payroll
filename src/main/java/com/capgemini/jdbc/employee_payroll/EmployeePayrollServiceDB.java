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
	private List<EmployeePayrollData> employeeList;

	private EmployeePayrollServiceDB() {
	}

	public static EmployeePayrollServiceDB getInstance() {
		if (employeePayrollServiceDB == null)
			employeePayrollServiceDB = new EmployeePayrollServiceDB();
		return employeePayrollServiceDB;
	}

	public void loadData() {
		try {
			this.employeeList = this.readData();
		} catch (SQLException e) {
		}
	}

	/**
	 * UC2
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<EmployeePayrollData> readData() throws SQLException {
		String sql = "select a.emp_id, a.comp_id, a.name, a.gender, a.address, a.phone_number,a.date_of_joining, b.net_pay "
				+ "from employee a, payroll b where a.emp_id = b.emp_id and a.is_active = true";
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
		String sql = "select a.emp_id, a.comp_id, a.name, a.gender, a.address, a.phone_number,a.date_of_joining, b.net_pay "
				+ "from employee a, payroll b where date_of_joining between date('" + date + "') and date(now()) and "
				+ "a.emp_id = b.emp_id and a.is_active = true";

		Connection connection = new EmployeePayrollDB().getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		return this.getEmployeePayrollData(resultSet);
	}

	private void getPreparedStatement(String name) {
		String sql = "select a.emp_id, a.comp_id, a.name, a.gender, a.address, a.phone_number,a.date_of_joining, b.net_pay "
				+ "from employee a, payroll b where a.name = '" + name + "' and "
				+ "a.emp_id = b.emp_id and a.is_active = true";
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
				int comp_id = resultSet.getInt("comp_id");
				String name = resultSet.getString("name");
				double salary = resultSet.getDouble("net_pay");
				String address = resultSet.getString("address");
				String phone_no = resultSet.getString("phone_number");
				String gender = resultSet.getString("gender");
				String date = resultSet.getString("date_of_joining");
				EmployeePayrollData data = new EmployeePayrollData(id, comp_id, name, gender, address, phone_no, salary,
						date);
				employeePayrollDatas.add(data);
			}

			Connection connection = new EmployeePayrollDB().getConnection();
			for (EmployeePayrollData e : employeePayrollDatas) {
				String sql = String.format("select comp_name from company where comp_id = %s", e.getComp_id());
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				if (result.next()) {
					String comp_name = result.getString("comp_name");
					e.setCompany_name(comp_name);
				}
				String sql_d = String.format("select a.emp_id, b.dept_name from employee_department a, department b "
						+ "where a.dept_id = b.dept_id and a.emp_id = %s", e.getEmp_id());
				Statement statement_d = connection.createStatement();
				ResultSet resultSet_d = statement_d.executeQuery(sql_d);
				List<String> departmentList = new ArrayList<>();
				while (resultSet_d.next()) {
					String department = resultSet_d.getString("dept_name");
					departmentList.add(department);
				}
				String[] departments = departmentList.toArray(new String[0]);
				e.setDepartment(departments);
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
	 * UC7 UC8 UC9 UC11
	 * 
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
			Date date, double salary, String comp_name, int comp_id, String[] department, int[] dept_id)
			throws DBException {
		EmployeePayrollData employeePayrollData = null;
		Connection connection = null;
		System.out.println("\n" + name + " is being added\n");

		/**
		 * Adding to company table
		 */
		try {
			connection = new EmployeePayrollDB().getConnection();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			List<Integer> company_id_list = new ArrayList<>();
			if (employeeList != null) {
				String sql = "select * from company";
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					Integer value = resultSet.getInt("comp_id");
					company_id_list.add(value);
				}
			}
			boolean toInsert = true;
			if (company_id_list.contains(comp_id))
				toInsert = false;
			if (toInsert) {
				String sql_company = String.format("insert into company values (%s, '%s')", comp_id, comp_name);
				statement.executeUpdate(sql_company);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/**
		 * Adding to department table
		 */
		try {
			Statement statement = connection.createStatement();
			List<Integer> dept_idList = new ArrayList<>();
			if (employeeList != null) {
				String sql = "select * from department";
				ResultSet resultSet = statement.executeQuery(sql);
				while (resultSet.next()) {
					Integer d_id = resultSet.getInt("dept_id");
					dept_idList.add(d_id);
				}
			}

			for (int index = 0; index < dept_id.length; index++) {
				boolean toInsert = true;
				for (Integer dep : dept_idList) {
					if (dept_id[index] == dep) {
						toInsert = false;
						break;
					}
				}
				if (toInsert == true) {
					Statement statement_d = connection.createStatement();
					String sql_department = String.format("insert into department values (%s,'%s')", dept_id[index],
							department[index]);
					statement_d.executeUpdate(sql_department);
				}
			}
		} catch (SQLException e) {
		}

		/**
		 * Adding to employee table
		 */
		try {
			Statement statement_employee = connection.createStatement();
			String sql = String.format("insert into employee"
					+ "(comp_id,emp_id,name,gender,phone_number,address,date_of_joining) values (%s,%s,'%s',"
					+ "'%s','%s','%s',date(now()))", comp_id, id, name, gender, phone_no, address);
			statement_employee.executeUpdate(sql, statement_employee.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DBException("Insertion error", Type.WRONG_QUERY);
			}
			return employeePayrollData;
		}

		/**
		 * Adding to payroll table
		 */
		double deductions = salary * 0.2;
		double taxable_pay = salary - deductions;
		double tax = taxable_pay * 0.1;
		double net_pay = taxable_pay - tax;
		String sql_salary = String.format("insert into payroll values (%s,%s,%s,%s,%s,%s)", id, salary, deductions,
				taxable_pay, tax, net_pay);
		try {
			Statement statement_salary = connection.createStatement();
			statement_salary.executeUpdate(sql_salary);
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new DBException("Insertion error", Type.WRONG_QUERY);
			}
		}

		/**
		 * Adding to employee department table
		 */
		try {
			Statement statement = connection.createStatement();
			for (int i = 0; i < dept_id.length; i++) {
				String sql_emp_department = String.format("insert into Employee_Department values (%s,%s)", id,
						dept_id[i]);
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

		/**
		 * committing the changes
		 */
		try {
			connection.commit();
			System.out.println("\n" + name + " added !!");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					throw new DBException("Connection not closed", Type.CONNECTION_ERROR);
				}
		}
		return employeePayrollData;
	}

	/**
	 * UC1_Thread
	 * 
	 * @param employeePayrollList
	 */
	public void addEmployees(List<EmployeePayrollData> employeePayrollList) {
		Map<Integer, Boolean> employeeStatus = new HashMap<>();
		for (EmployeePayrollData employee : employeePayrollList) {
			Runnable task = () -> {
				employeeStatus.put(employee.hashCode(), false);
				try {
					this.addNewEmployee(employee.getEmp_id(), employee.getName(), employee.getGender().charAt(0),
							employee.getPhone_no(), employee.getAddress(), Date.valueOf(employee.getStart_date()),
							employee.getSalary(), employee.getCompany_name(), employee.getComp_id(),
							employee.getDepartment(), employee.getDept_id());
					employeeStatus.put(employee.hashCode(), true);
				} catch (DBException e1) {
					System.out.println(e1.getMessage());
				}
				employeeStatus.put(employee.hashCode(), true);
			};
			Thread thread = new Thread(task, employee.getName());
			thread.start();
		}
		while (employeeStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * UC12
	 * 
	 * @param name
	 */
	public void removeEmployee(String name) {
		String sql = String.format("update employee set is_active = %s where name = '%s'", false, name);
		Connection connection = new EmployeePayrollDB().getConnection();
		try {
			Statement statement = connection.createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
		}

	}
}
