package trainingsapp;

import trainingsapp.backend.BackendSession;
import trainingsapp.backend.Training;

public class Runner extends Thread {

    private BackendSession session;
    private int id;
    private Training training;

    public Runner(BackendSession session, int id, Training training) {
        this.session = session;        
        this.id = id;
        this.training = training;
    }

    public void run() {
        // TODO
    }
    
}
