package trainingsapp.backend;

import java.util.UUID;

class Reservation {
    public String user;
    public String training;
    public long reservationTime;
    
    public Reservation(String user, String training) {
        this.user = user;
        this.training = training;
    }

    public Reservation(String user, String training, long reservationTime) {
        this.user = user;
        this.training = training;
        this.reservationTime = reservationTime;
    }

    public boolean equals(Reservation reservation) {
        return this.user.equals(reservation.user) &&
               this.training.equals(reservation.training) &&
               this.reservationTime == reservation.reservationTime;
    }
}