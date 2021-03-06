package trainingsapp.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;

/*
 * TODO:
 * Performing stress tests often results in numerous WriteTimeoutExceptions, 
 * ReadTimeoutExceptions (thrown by Cassandra replicas) and 
 * OpetationTimedOutExceptions (thrown by the client). Remember to retry
 * failed operations until success (it can be done through the RetryPolicy mechanism:
 * https://stackoverflow.com/questions/30329956/cassandra-datastax-driver-retry-policy )
 */

public class BackendSession {

	private static final Logger logger = LoggerFactory.getLogger(BackendSession.class);

	private final Session session;

	public UserController userController;
	public RoomController roomController;
	public ReservationController reservationController;
	public TrainingController trainingController;

	public BackendSession(String contactPoint, String keyspace) throws BackendException {

		Cluster cluster = Cluster.builder().addContactPoint(contactPoint).withQueryOptions(
			new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE)
		).build();
		try {
			session = cluster.connect(keyspace);
		} catch (Exception e) {
			throw new BackendException("Could not connect to the cluster. " + e.getMessage() + ".", e);
		}
		init();
	}

	// private static final String USER_FORMAT = "- %-10s  %-16s %-10s %-10s\n";

	private void init() throws BackendException {
		try {
			this.userController = new UserController(session);
			this.roomController = new RoomController(session);
			this.reservationController = new ReservationController(session);
			this.trainingController = new TrainingController(session);
		} catch (Exception e) {
			throw new BackendException("Could not initialize backend. " + e.getMessage() + ".", e);
		}

		logger.info("Backed initialized");
	}

}
