package trainingsapp.backend;

import com.datastax.driver.core.*;

class RoomController {

    private final Session session;


    public RoomController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() {
        try {
            SELECT_ALL_ROOMS = session.prepare("");
        } catch (Execution e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public createRoom() {}

    public deleteRoom() {}

    public selectRoomById() {}
}