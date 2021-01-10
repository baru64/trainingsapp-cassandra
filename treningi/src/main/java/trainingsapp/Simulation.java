package trainingsapp;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;

class Simulation {

    private BackendSession backendSession;

    public Simulation(BackendSession session) {
        this.backendSession = session;
    }
    
    public void run() {
        // TODO: run multiple threads simulating clients
    }
}