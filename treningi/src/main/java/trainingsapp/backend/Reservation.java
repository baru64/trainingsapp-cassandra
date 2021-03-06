package trainingsapp.backend;

public class Reservation {
    public String user;
    public String userName;
    public String training;
    public String trainingName;
    public long reservationTime;
    
    public Reservation(String user, String userName, String training, String trainingName) {
        this.user = user;
        this.userName = userName;
        this.training = training;
        this.trainingName = trainingName;
    }

    public Reservation(String user, String userName, String training, String trainingName, long reservationTime) {
        this.user = user;
        this.userName = userName;
        this.training = training;
        this.trainingName = trainingName;
        this.reservationTime = reservationTime;
    }

    public boolean equals(Reservation reservation) {
        return this.user.equals(reservation.user) &&
               this.training.equals(reservation.training);
    }

    public int compareTimestamp(Reservation reservation) {
        if (this.reservationTime == reservation.reservationTime) return 0;
        else if (this.reservationTime > reservation.reservationTime) return 1;
        else return -1;
    }
}
