package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagemenSystem {
	private static final String url = "jdbc:mySQL://localhost:3306/hospital";
	private static final String username = "root";
	private static final String password = "12345";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		try {
			Connection connection = DriverManager.getConnection(url, username, password);
			Patient patient = new Patient(connection, scanner);
			Doctor doctor = new Doctor(connection);

			while (true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM");
				System.out.println("1. Add  Patient");
				System.out.println("2. View Patient");
				System.out.println("3. View  Doctors");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter your choice: ");
				int choice = scanner.nextInt();
				switch (choice) {
				case 1:
					patient.addPatient();
					System.out.println();
					break;
				case 2:
					patient.viewPatient();
					System.out.println();
					break;
				case 3:
					doctor.viewDoctors();
					System.out.println();
					break;
				case 4:
					bookAppointment(patient, doctor, connection, scanner);
					break;
				case 5:
					System.out.println("THANK YOU!! FOR USING HOSPITAL MANAGEMENT SYSTEM");
					return;
				default:
					System.out.println("enter valid choice");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
	}

	public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
		System.out.println("Enter Patient ID: ");
		int patientID = scanner.nextInt();
		System.out.println("Enter Doctor ID: ");
		int doctortID = scanner.nextInt();
		System.out.println("Enter appointment datae (YYY-MM-DD): ");
		String appointmentDate = scanner.next();
		if (patient.getPatientById(patientID) && doctor.getDoctorById(doctortID)) {
			if (chcekDoctorAvailablity(doctortID, appointmentDate, connection)) {
				String appointmentQuery = "insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
					preparedStatement.setInt(1, patientID);
					preparedStatement.setInt(2, doctortID);
					preparedStatement.setString(3, appointmentDate);
					int rowsAffected = preparedStatement.executeUpdate();
					if (rowsAffected > 0) {
						System.out.println("Appointment booked");
					} else {
						System.out.println("failed to book appointment");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println("Doctor  not available on this date");
			}

		} else {
			System.out.println("Either doctor or patient doesn't exist!!!");
		}

	}

	public static boolean chcekDoctorAvailablity(int doctorID, String appointmentDate, Connection connection) {
		String query = "select  count(*) from appointments where doctor_id=? and appointment_date=?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, doctorID);
			preparedStatement.setString(2, appointmentDate);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				if (count == 0) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

}
