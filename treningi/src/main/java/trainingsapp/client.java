package trainingsapp;

import java.util.UUID;

import trainingsapp.backend.BackendException;
import trainingsapp.backend.BackendSession;

class Client {

    private BackendSession backendSession;
    private UUID userId;
    private String selectedTraining;
    private int selectedTimeslot;

    public Client(BackendSession session) {
        this.backendSession = session;
    }
}