package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

	private Connection connection;
	private Scanner scanner;

	public Patient(Connection connection, Scanner scanner) {

		this.connection = connection;
		this.scanner = scanner;
	}

	public void addPatient() {
		System.out.print("enter patient name");
		String name = scanner.next();
		System.out.print("enter patient age");
		int age = scanner.nextInt();
		System.out.print("enter patient gender ");
		String gender = scanner.next();

		try {

			String query = "insert into patient(name,age,gender) values (?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, age);
			preparedStatement.setString(3, gender);
			int affctedrows = preparedStatement.executeUpdate();
			if (affctedrows > 0) {
				System.out.println(" patient added sucessfully");
			} else {
				System.out.println("failed to add patient");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void viewPatient() {
		String query = "select * from patient";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			System.out.println("petient: ");
			System.out.println("+-----------+-------------------+---------+----------+");
			System.out.println("|patient Id |Name               |Age      |Gender    |");
			System.out.println("+-----------+-------------------+---------+----------|");
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				int age = resultSet.getInt("age");
				String gender = resultSet.getString("gender");
				System.out.printf("|%-12s|%-19s|%-9s|%-10s|\n", id, name, age, gender);
				System.out.println("+-----------+-------------------+---------+----------|");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean getPatientById(int id) {
		String query = "select * from patient where id=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}