package trainingsapp.backend;

import com.datastax.driver.core.*;

class TrainingController {

    private final Session session;


    public TrainingController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() {
        try {
            SELECT_ALL_TRAININGS = session.prepare("");
        } catch (Execution e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public createTraining() {}

    // deleteUser - deletes training and related reservations
    public deleteTraining() {}

    public selectAllTrainings() {}

    public selectTrainingByName() {}
    
    public selectTrainingByTimeslot() {}
}