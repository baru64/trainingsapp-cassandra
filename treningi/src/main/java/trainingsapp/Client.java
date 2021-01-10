package trainingsapp;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.clearspring.analytics.util.Pair;

import trainingsapp.backend.*;

class Client {

    private BackendSession backendSession;
    private String userName;
    private String userId;
    private Reservation reservation;
    private Training training;

    public Client(BackendSession session, String userName, String userId) {
        this.backendSession = session;
	this.userName = userName;
	this.userId = userId;
    }

    public Reservation makeReservation(Training training) throws BackendException {
        backendSession.reservationController.createReservation(userId, userName, training.trainingId, training.name);
        Reservation reservation = null;
	while(reservation == null) reservation = backendSession.reservationController
                                    .selectReservationByUser(userId, training.trainingId);
        this.reservation = reservation;
        // save training for later
        this.training = training;
        return reservation;
    }

    public void cancelReservation() throws BackendException {
        backendSession.reservationController.deleteReservation(this.reservation.training, this.userId);
        this.reservation = null;
        this.training = null;
    }

    private <T> int linkedListComparator(LinkedList<T> a, LinkedList<T> b) {
        if (a.size() == b.size()) {
            return 0;
        } else if (a.size() > b.size()) {
            return 1;
        } else {
            return -1;
        }
    }

    private int findReservation(LinkedList<Reservation> list, Reservation reservation) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).equals(reservation)) return i;
	}
	return -1;
    }

    public ReservationStatus getReservationStatus() throws BackendException {
        LinkedList<Reservation> reservations = backendSession.reservationController
                                                .selectReservationsByTraining(this.reservation.training);
        LinkedList<Room> rooms = backendSession.roomController.selectAllRooms();
        LinkedList<Training> trainings = backendSession.trainingController.selectTrainingsByTime(training.timeslot);
        // group reservations
        LinkedList<Pair<String, LinkedList<Reservation>>> reservationsGrouped =
            new LinkedList<Pair<String, LinkedList<Reservation>>>();
        for (Training t : trainings) {
            LinkedList<Reservation> relatedReservations = reservations.stream().filter(
                r -> r.training.equals(t.trainingId)
            ).collect(Collectors.toCollection(LinkedList::new));
            reservationsGrouped.add(new Pair<String, LinkedList<Reservation>>(t.name, relatedReservations));
        }
        // sort reservations and get one training per room
        reservationsGrouped.sort((a, b) -> linkedListComparator(b.right, a.right));
        List<Pair<String, LinkedList<Reservation>>> acceptedReservations = reservationsGrouped.subList(0, rooms.size());
        rooms.sort((a,b) -> b.compareTo(a));
        boolean isAccepted = false;
        boolean isOnReserveList = false;
        int reserveListPosition = 0;
        for (int i = 0; i < acceptedReservations.size(); ++i) {
            acceptedReservations.get(i).right.sort((a, b) -> b.compareTimestamp(a));
            int index = findReservation(acceptedReservations.get(i).right, this.reservation);
            if (index != -1) {
                isAccepted = true;
                if (index > rooms.get(i).capacity) {
                    isOnReserveList = true;
                    reserveListPosition = index - rooms.get(i).capacity;
                }
            }
        }
        return new ReservationStatus(reservation, isAccepted, isOnReserveList, reserveListPosition);
    }
}
