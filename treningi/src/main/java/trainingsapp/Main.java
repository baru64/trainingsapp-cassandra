package trainingsapp;

import java.io.IOException;
import java.util.Properties;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";

	public static void insertInitialData(BackendSession session) throws BackendException {
		session.userController.createUser("pszemek", 123000333);
		session.userController.createUser("piotrek", 123000333);
		session.userController.createUser("asia", 123000333);
		session.userController.createUser("ania", 123000333);
		session.userController.createUser("gosia", 123000333);
		session.trainingController.createTraining("kettlebell", 1);
		session.trainingController.createTraining("yoga", 1);
		session.trainingController.createTraining("fitness", 2);
		session.trainingController.createTraining("functional", 2);
		session.trainingController.createTraining("dryswimming", 2);
		session.roomController.createRoom(15);
		session.roomController.createRoom(10);
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

		if(args[0] == "--simulation"){
			Simulation sim = new Simulation(session);
			sim.start(args);
		}
		Main.insertInitialData(session);
		Cli cli = new Cli(session);
		cli.cmdLoop();

		System.exit(0);
	}
}
