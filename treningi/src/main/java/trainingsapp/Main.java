package trainingsapp;

import java.io.IOException;
import java.util.Properties;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;
import trainingsapp.backend.Training;
import trainingsapp.backend.User;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";

	public static void main(String[] args) throws IOException, BackendException {
		String contactPoint = null;
		String keyspace = null;

		Properties properties = new Properties();
		try {
			properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));

			contactPoint = properties.getProperty("contact_point");
			keyspace = properties.getProperty("keyspace");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
			
		BackendSession session = new BackendSession(contactPoint, keyspace);

		// add users
		User clientUser = session.userController.createUser("pszemek", 123000333);
		session.userController.createUser("piotrek", 123000333);
		session.userController.createUser("asia", 123000333);
		session.userController.createUser("ania", 123000333);
		session.userController.createUser("gosia", 123000333);
		// add trainings
		Training t1 = session.trainingController.createTraining("kettlebell", 1);
		session.trainingController.createTraining("yoga", 1);
		session.trainingController.createTraining("fitness", 2);
		session.trainingController.createTraining("functional", 2);
		session.trainingController.createTraining("dryswimming", 2);
		// add rooms
		session.roomController.createRoom(15);
		session.roomController.createRoom(10);

		Client client = new Client(session, clientUser.name, clientUser.userId);
		client.makeReservation(t1);
		ReservationStatus rs = client.getReservationStatus();
		System.out.println("reservation submitted");
		if (rs.isAccepted) System.out.println("accepted");
		if (rs.isOnReserveList) System.out.println("on reserve list");

		System.exit(0);
	}
}
