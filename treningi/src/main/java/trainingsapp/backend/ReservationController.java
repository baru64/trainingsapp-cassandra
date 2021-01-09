package trainingsapp.backend;

import java.util.LinkedList;

import com.datastax.driver.core.*;

class ReservationController {

    private final Session session;


    public ReservationController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() {
        try {
            SELECT_ALL_RESERVATIONS = session.prepare("SELECT * FROM reservations;");
            SELECT_RESERVATIONS_BY_TRAINING = session.prepare("SELECT * FROM reservations WHERE trainingName=?;");
            SELECT_RESERVATION_BY_PK = session.prepare("SELECT * FROM users WHERE user=?;");
            INSERT_RESERVATION = session.prepare("INSERT INTO reservations (userId, trainingName, timeslot) VALUES (?,?,?);");
            DELETE_RESERVATION = session.prepare("DELETE FROM reservations WHERE trainingName=? AND user=?;");
        } catch (Execution e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public Reservation createReservation(String userId, String trainingName, int timeslot) {}

    public void deleteReservation() {}

    public LinkedList<Reservation> selectAllReservations() {}

    public LinkedList<Reservation> selectReservationsByTraining() {}

    public Reservation selectReservationByUser(User user) {}
}