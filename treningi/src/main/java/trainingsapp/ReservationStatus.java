package trainingsapp;

import trainingsapp.backend.Reservation;

public class ReservationStatus {
    public Reservation reservation;
    public boolean isOnReserveList;
    public int reserveListPosition;

    public ReservationStatus(Reservation reservation, boolean isOnReserveList, int reserveListPosition) {
        this.reservation = reservation;
        this.isOnReserveList = isOnReserveList;
        this.reserveListPosition = reserveListPosition;
    }
}
