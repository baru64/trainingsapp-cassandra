package trainingsapp.backend;

import java.util.LinkedList;
import java.util.UUID;

import com.datastax.driver.core.*;

public class ReservationController {

    private final Session session;

    private static PreparedStatement SELECT_ALL_RESERVATIONS;
    private static PreparedStatement SELECT_RESERVATIONS_BY_TRAINING;
    private static PreparedStatement SELECT_RESERVATION_BY_USER;
    private static PreparedStatement INSERT_RESERVATION;
    private static PreparedStatement DELETE_RESERVATION;
    private static PreparedStatement DELETE_ALL;

    public ReservationController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() throws BackendException {
        try {
            SELECT_ALL_RESERVATIONS = session.prepare("SELECT * FROM reservations;");
            SELECT_RESERVATIONS_BY_TRAINING = session.prepare("SELECT * FROM reservations WHERE training=?;");
            SELECT_RESERVATION_BY_USER = session.prepare("SELECT * FROM reservations WHERE training=? AND user=?;");
            INSERT_RESERVATION = session.prepare(
                "INSERT INTO reservations (user, userName, training, trainingName, reservationTime) VALUES (?,?,?,?,toTimeStamp(now()));"
            );
            DELETE_RESERVATION = session.prepare("DELETE FROM reservations WHERE training=? AND user=?;");
            DELETE_ALL = session.prepare("TRUNCATE reservations;");
        } catch (Exception e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public void createReservation(String userId, String userName, String trainingId, String trainingName)
    throws BackendException {
        BoundStatement insertReservation = new BoundStatement(INSERT_RESERVATION);
        BoundStatement selectReservation = new BoundStatement(SELECT_RESERVATION_BY_USER);
        ResultSet rs = null;
        Reservation reservation = new Reservation(userId, userName, trainingId, trainingName);
        try {
            selectReservation.bind(UUID.fromString(trainingId), UUID.fromString(userId));
            rs = session.execute(selectReservation);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
        if (rs.iterator().hasNext()) {
            throw new BackendException("Reservation for this user exists.");
        } else {
            try {
                insertReservation.bind(
                    UUID.fromString(reservation.user), reservation.userName,
                    UUID.fromString(reservation.training), reservation.trainingName
                );
                session.execute(insertReservation);
            } catch (Exception e) {
                throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
            }
        }
    }

    public void deleteReservation(String userId, String trainingId) throws BackendException {
        BoundStatement deleteReservation = new BoundStatement(DELETE_RESERVATION);
        try {
            deleteReservation.bind(UUID.fromString(trainingId), UUID.fromString(trainingId));
            session.execute(deleteReservation);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public void deleteAllReservations() throws BackendException {
        BoundStatement deleteAll = new BoundStatement(DELETE_ALL);
        try {
            session.execute(deleteAll);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public LinkedList<Reservation> selectAllReservations() throws BackendException {
        LinkedList<Reservation> reservations = new LinkedList<>();
        BoundStatement selectAllReservations = new BoundStatement(SELECT_ALL_RESERVATIONS);
        ResultSet rs = null;
        try {
            rs = session.execute(selectAllReservations);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String user = row.getUUID("user").toString();
            String userName = row.getString("userName");
            String trainingId = row.getUUID("trainingId").toString();
            String trainingName = row.getString("trainingName");
            long reservationTime = row.getTimestamp("reservationTime").getTime();

            reservations.add(new Reservation(user, userName, trainingId, trainingName, reservationTime));
        }
        return reservations;
    }

    public LinkedList<Reservation> selectReservationsByTraining(String trainingId) throws BackendException {
        LinkedList<Reservation> reservations = new LinkedList<>();
        BoundStatement selectReservations = new BoundStatement(SELECT_RESERVATIONS_BY_TRAINING);
        ResultSet rs = null;
        try {
            selectReservations.bind(UUID.fromString(trainingId));
            rs = session.execute(selectReservations);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String user = row.getUUID("user").toString();
            String userName = row.getString("userName");
            String trainingName = row.getString("trainingName");
            long reservationTime = row.getTimestamp("reservationTime").getTime();

            reservations.add(new Reservation(user, userName, trainingId, trainingName, reservationTime));
        }
        return reservations;
    }

    public Reservation selectReservationByUser(String userId, String trainingId) throws BackendException {
        BoundStatement selectReservation = new BoundStatement(SELECT_RESERVATION_BY_USER);
        ResultSet rs = null;
        try {
            selectReservation.bind(UUID.fromString(trainingId), UUID.fromString(userId));
            rs = session.execute(selectReservation);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
	for (Row row : rs) {
        	String userName = row.getString("userName");
        	String trainingName = row.getString("trainingName");
        	long reservationTime = row.getTimestamp("reservationTime").getTime();
        	return new Reservation(userId, userName, trainingId, trainingName, reservationTime);
        }
	return null;
    }
}
