package trainingsapp.backend;

import java.util.UUID;

class Reservation {
    public String user;
    public String trainingName;
    public int timeslot;
    public long reservationTime;

    public Reservation(String user, String trainingName, int timeslot, long reservationTime) {
        this.user = user;
        this.trainingName = trainingName;
        this.timeslot = timeslot;
        this.reservationTime = reservationTime;
    }

    public boolean equals(Reservation reservation) {
        return this.user.equals(reservation.user) &&
               this.trainingName.equals(reservation.trainingName) &&
               this.timeslot == reservation.timeslot &&
               this.reservationTime == reservation.timeslot;
    }
}