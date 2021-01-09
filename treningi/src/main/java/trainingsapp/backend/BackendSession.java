package trainingsapp.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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

		Cluster cluster = Cluster.builder().addContactPoint(contactPoint).build();
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

	// public String selectAll() throws BackendException {
	// 	StringBuilder builder = new StringBuilder();
	// 	BoundStatement bs = new BoundStatement(SELECT_ALL_FROM_USERS);

	// 	ResultSet rs = null;

	// 	try {
	// 		rs = session.execute(bs);
	// 	} catch (Exception e) {
	// 		throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
	// 	}

	// 	for (Row row : rs) {
	// 		String rcompanyName = row.getString("companyName");
	// 		String rname = row.getString("name");
	// 		int rphone = row.getInt("phone");
	// 		String rstreet = row.getString("street");

	// 		builder.append(String.format(USER_FORMAT, rcompanyName, rname, rphone, rstreet));
	// 	}

	// 	return builder.toString();
	// }

	// public void upsertUser(String companyName, String name, int phone, String street) throws BackendException {
	// 	BoundStatement bs = new BoundStatement(INSERT_INTO_USERS);
	// 	bs.bind(companyName, name, phone, street);

	// 	try {
	// 		session.execute(bs);
	// 	} catch (Exception e) {
	// 		throw new BackendException("Could not perform an upsert. " + e.getMessage() + ".", e);
	// 	}

	// 	logger.info("User " + name + " upserted");
	// }

	// public void deleteAll() throws BackendException {
	// 	BoundStatement bs = new BoundStatement(DELETE_ALL_FROM_USERS);

	// 	try {
	// 		session.execute(bs);
	// 	} catch (Exception e) {
	// 		throw new BackendException("Could not perform a delete operation. " + e.getMessage() + ".", e);
	// 	}

	// 	logger.info("All users deleted");
	// }

	// protected void finalize() {
	// 	try {
	// 		if (session != null) {
	// 			session.getCluster().close();
	// 		}
	// 	} catch (Exception e) {
	// 		logger.error("Could not close existing cluster", e);
	// 	}
	// }

}
