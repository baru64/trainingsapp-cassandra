package trainingsapp;

import java.util.LinkedList;

import trainingsapp.backend.*;

class Client {

    private BackendSession backendSession;
    private String userName;
    private String userId;
    private Reservation reservation;

    public Client(BackendSession session, String userName, String userId) {
        this.backendSession = session;
    }

    public Reservation makeReservation(String trainingId, String trainingName) throws BackendException {
        backendSession.reservationController.createReservation(userId, userName, trainingId, trainingName);
        Reservation reservation = backendSession.reservationController.selectReservationByUser(userId, trainingId);
        this.reservation = reservation;
        return reservation;
    }

    public void cancelReservation() throws BackendException {
        backendSession.reservationController.deleteReservation(this.reservation.training, this.userId);
        this.reservation = null;
    }

    public ReservationStatus getReservationStatus() throws BackendException {
        LinkedList<Reservation> reservations = backendSession.reservationController
                                                .selectReservationsByTraining(this.reservation.training);
        LinkedList<Room> rooms = backendSession.roomController.selectAllRooms();
        // TODO: sort and calculate if on reserve list
        return new ReservationStatus(reservation, true, 2);
    }
}