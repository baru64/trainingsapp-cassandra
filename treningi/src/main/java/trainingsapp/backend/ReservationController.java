package trainingsapp.backend;

import java.util.LinkedList;

import com.datastax.driver.core.*;

class ReservationController {

    private final Session session;

    private static PreparedStatement SELECT_ALL_RESERVATIONS;
    private static PreparedStatement SELECT_RESERVATIONS_BY_TRAINING;
    private static PreparedStatement SELECT_RESERVATION_BY_USER;
    private static PreparedStatement INSERT_RESERVATION;
    private static PreparedStatement DELETE_RESERVATION;

    public ReservationController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() throws BackendException {
        try {
            SELECT_ALL_RESERVATIONS = session.prepare("SELECT * FROM reservations;");
            SELECT_RESERVATIONS_BY_TRAINING = session.prepare("SELECT * FROM reservations WHERE trainingId=?;");
            SELECT_RESERVATION_BY_USER = session.prepare("SELECT * FROM users WHERE user=?;");
            INSERT_RESERVATION = session.prepare("INSERT INTO reservations (user, trainingId) VALUES (?,?);");
            DELETE_RESERVATION = session.prepare("DELETE FROM reservations WHERE user=?;");
        } catch (Exception e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public void createReservation(String userId, String trainingId) throws BackendException {
        BoundStatement insertReservation = new BoundStatement(INSERT_RESERVATION);
        BoundStatement selectReservation = new BoundStatement(SELECT_RESERVATION_BY_USER);
        ResultSet rs = null;
        Reservation reservation = new Reservation(userId, trainingId);
        try {
            selectReservation.bind(userId);
            rs = session.execute(selectReservation);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
        if (rs.iterator().hasNext()) {
            throw new BackendException("Reservation for this user exists.");
        } else {
            try {
                insertReservation.bind(reservation.user, reservation.training);
                session.execute(insertReservation);
            } catch (Exception e) {
                throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
            }
        }
    }

    public void deleteReservation(String userId) throws BackendException {
        BoundStatement deleteReservation = new BoundStatement(DELETE_RESERVATION);
        try {
            deleteReservation.bind(userId);
            session.execute(deleteReservation);
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
            String trainingId = row.getUUID("trainingId").toString();
            long reservationTime = row.getTimestamp("reservationTime").getTime();

            reservations.add(new Reservation(user, trainingId, reservationTime));
        }
        return reservations;
    }

    public LinkedList<Reservation> selectReservationsByTraining(String trainingId) throws BackendException {
        LinkedList<Reservation> reservations = new LinkedList<>();
        BoundStatement selectReservations = new BoundStatement(SELECT_RESERVATIONS_BY_TRAINING);
        ResultSet rs = null;
        try {
            selectReservations.bind(trainingId);
            rs = session.execute(selectReservations);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String user = row.getUUID("user").toString();
            long reservationTime = row.getTimestamp("reservationTime").getTime();

            reservations.add(new Reservation(user, trainingId, reservationTime));
        }
        return reservations;
    }

    public Reservation selectReservationByUser(String userId) throws BackendException {
        BoundStatement selectReservation = new BoundStatement(SELECT_RESERVATION_BY_USER);
        ResultSet rs = null;
        try {
            selectReservation.bind(userId);
            rs = session.execute(selectReservation);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        String trainingId = rs.one().getUUID("trainingId").toString();
        int timeslot = rs.one().getInt("timeslot");
        long reservationTime = rs.one().getTimestamp("reservationTime").getTime();
        

        return new Reservation(userId, trainingId, reservationTime);
    }
}