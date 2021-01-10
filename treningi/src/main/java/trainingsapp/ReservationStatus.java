package trainingsapp;

import trainingsapp.backend.Reservation;

public class ReservationStatus {
    public Reservation reservation;
    public boolean isAccepted;
    public boolean isOnReserveList;
    public int reserveListPosition;

    public ReservationStatus(Reservation reservation, boolean isAccepted, boolean isOnReserveList, int reserveListPosition) {
        this.reservation = reservation;
        this.isAccepted = isAccepted;
        this.isOnReserveList = isOnReserveList;
        this.reserveListPosition = reserveListPosition;
    }
}
