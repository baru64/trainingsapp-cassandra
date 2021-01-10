package trainingsapp.backend;

import java.util.UUID;

public class Training {
    public String trainingId;
    public String name;
    public int timeslot;

    public Training(String name, int timeslot) {
        this.name = name;
        this.timeslot = timeslot;
        this.trainingId = UUID.randomUUID().toString();
    }

    public Training(String trainingId, String name, int timeslot) {
        this.name = name;
        this.timeslot = timeslot;
        this.trainingId = trainingId;
    }

    public boolean equals(Training training) {
        return this.name.equals(training.name) &&
               this.timeslot == training.timeslot;
    }
}