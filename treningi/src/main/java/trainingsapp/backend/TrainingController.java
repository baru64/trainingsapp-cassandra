package trainingsapp.backend;

import java.util.LinkedList;
import java.util.UUID;

import com.datastax.driver.core.*;

public class TrainingController {

    private final Session session;

    private static PreparedStatement SELECT_ALL_TRAININGS;
    private static PreparedStatement SELECT_TRAININGS_BY_TIME;
    // private static PreparedStatement SELECT_TRAINING_BY_NAME_AND_TIME;
    // private static PreparedStatement SELECT_TRAINING_BY_ID;
    private static PreparedStatement INSERT_TRAINING;
    private static PreparedStatement DELETE_TRAINING;
    private static PreparedStatement DELETE_ALL;

    public TrainingController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() throws BackendException {
        try {
            SELECT_ALL_TRAININGS = session.prepare("SELECT * FROM trainings;");
            SELECT_TRAININGS_BY_TIME = session.prepare("SELECT * FROM trainings WHERE timeslot=?;");
            // SELECT_TRAINING_BY_NAME_AND_TIME = session.prepare("SELECT * FROM trainings WHERE name=? AND timeslot=?;");
            // SELECT_TRAINING_BY_ID = session.prepare("SELECT * FROM trainings WHERE timeslot=? AND name=? AND trainingId=?;");
            INSERT_TRAINING = session.prepare("INSERT INTO trainings (trainingId, name, timeslot) VALUES (?,?,?);");
            DELETE_TRAINING = session.prepare("DELETE FROM trainings WHERE timeslot=? AND name=? AND trainingId=?;");
            DELETE_ALL = session.prepare("TRUNCATE trainings;");
        } catch (Exception e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public Training createTraining(String name, int timeslot) throws BackendException {
        BoundStatement insertTraining = new BoundStatement(INSERT_TRAINING);
        // Assume that two same trainings can exist at the same time
        // BoundStatement selectTraining = new BoundStatement(SELECT_TRAINING_BY_NAME_AND_TIME);
        // ResultSet rs = null;
        Training training = new Training(name, timeslot);
        // try {
        //     selectTraining.bind(training.name, training.timeslot);
        //     rs = session.execute(selectTraining);
        // } catch (Exception e) {
        //     throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        // }
        // if (rs.iterator().hasNext()) {
        //     throw new BackendException("Training already exists.");
        // } else {
        try {
            insertTraining.bind(UUID.fromString(training.trainingId), training.name, training.timeslot);
            session.execute(insertTraining);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
        // }
        return training;
    }

    public void deleteTraining(String name, int timeslot, String trainingId) throws BackendException {
        BoundStatement deleteTraining = new BoundStatement(DELETE_TRAINING);
        try {
            deleteTraining.bind(timeslot, name, UUID.fromString(trainingId));
            session.execute(deleteTraining);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public void deleteAllTrainings() throws BackendException {
        BoundStatement deleteAll = new BoundStatement(DELETE_ALL);
        try {
            session.execute(deleteAll);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public LinkedList<Training> selectAllTrainings() throws BackendException {
        LinkedList<Training> trainings = new LinkedList<>();
        BoundStatement selectAllTrainings = new BoundStatement(SELECT_ALL_TRAININGS);
        ResultSet rs = null;
        try {
            rs = session.execute(selectAllTrainings);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String trainingId = row.getUUID("trainingId").toString();
            String name = row.getString("name");
            int timeslot = row.getInt("timeslot");

            trainings.add(new Training(trainingId, name, timeslot));
        }
        return trainings;
    }

    public LinkedList<Training> selectTrainingsByTime(int timeslot) throws BackendException {
        LinkedList<Training> trainings = new LinkedList<>();
        BoundStatement selectTrainings = new BoundStatement(SELECT_TRAININGS_BY_TIME);
        ResultSet rs = null;
        try {
            selectTrainings.bind(timeslot);
            rs = session.execute(selectTrainings);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String trainingId = row.getUUID("trainingId").toString();
            String name = row.getString("name");

            trainings.add(new Training(trainingId, name, timeslot));
        }
        return trainings;
    }
    
    // public Training selectTrainingById(String trainingId, int timeslot, String trainingName) throws BackendException {
    //     BoundStatement selectTraining = new BoundStatement(SELECT_TRAINING_BY_ID);
    //     ResultSet rs = null;
    //     try {
    //         selectTraining.bind(timeslot, trainingName, trainingId);
    //         rs = session.execute(selectTraining);
    //     } catch (Exception e) {
    //         throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
    //     }

    //     String name = rs.one().getString("name");
    //     int timeslot = rs.one().getInt("timeslot");

    //     return new Training(trainingId, name, timeslot);
    // }
}