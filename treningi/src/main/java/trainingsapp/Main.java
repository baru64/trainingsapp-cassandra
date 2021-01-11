package trainingsapp;

import java.io.IOException;
import java.util.Properties;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";

	public static void insertInitialData(BackendSession session) throws BackendException {
		session.trainingController.createTraining("kettlebell", 1);
		session.trainingController.createTraining("yoga", 1);
		session.trainingController.createTraining("fitness", 2);
		session.trainingController.createTraining("functional", 2);
		session.trainingController.createTraining("dryswimming", 2);
		session.roomController.createRoom(2);
	}

	public static void cleanUp(BackendSession session) throws BackendException {
		session.reservationController.deleteAllReservations();
		session.userController.deleteAllUsers();
		session.roomController.deleteAllRooms();
		session.trainingController.deleteAllTrainings();
	}

	public static void main(String[] args) throws IOException, BackendException, InterruptedException {
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

		if (args.length != 0 && args[0].equals("--simulation")) {
			Simulation sim = new Simulation(session);
			sim.start(args);
			System.exit(0);
		}

		if (args.length != 0 && args[0].equals("--cleardb")) {
			// cleanup
			try {
				Main.cleanUp(session);
			} catch (BackendException e) {
            			System.out.println("Could not perform a query. " + e.getMessage() + ".");
			}
            		System.out.println("db cleared");
			System.exit(0);
		}

		if (args.length != 0 && args[0].equals("--insertdata")) {
			Main.insertInitialData(session);
		}
		
		Cli cli = new Cli(session);
		cli.cmdLoop();

		System.exit(0);
	}
}
