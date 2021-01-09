package trainingsapp.backend;

class Training {
    public String name;
    public int timeslot;

    public Training(String name, int timeslot) {
        this.name = name;
        this.timeslot = timeslot;
    }

    public boolean equals(Training training) {
        return this.name.equals(training.name) &&
               this.timeslot == training.timeslot;
    }
}