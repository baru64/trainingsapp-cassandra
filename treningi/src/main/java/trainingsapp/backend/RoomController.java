package trainingsapp.backend;

import java.util.LinkedList;
import java.util.UUID;

import com.datastax.driver.core.*;

public class RoomController {

    private final Session session;

    private static PreparedStatement SELECT_ALL_ROOMS;
    private static PreparedStatement INSERT_ROOM;
    private static PreparedStatement DELETE_ROOM;

    public RoomController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() throws BackendException {
        try {
            SELECT_ALL_ROOMS = session.prepare("SELECT * FROM rooms;");
            INSERT_ROOM = session.prepare("INSERT INTO rooms (roomId, capacity) VALUES (?,?);");
            DELETE_ROOM = session.prepare("DELETE FROM rooms WHERE roomId=?;");
        } catch (Exception e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public Room createRoom(int capacity) throws BackendException {
        BoundStatement insertRoom = new BoundStatement(INSERT_ROOM);
        Room room = new Room(capacity);
        try {
            insertRoom.bind(UUID.fromString(room.roomId), room.capacity);
            session.execute(insertRoom);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
        return room;
    }

    public void deleteRoom(String roomId) throws BackendException {
        BoundStatement deleteRoom = new BoundStatement(DELETE_ROOM);
        try {
            deleteRoom.bind(UUID.fromString(roomId));
            session.execute(deleteRoom);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public LinkedList<Room> selectAllRooms() throws BackendException {
        LinkedList<Room> rooms = new LinkedList<>();
        BoundStatement selectAllRooms = new BoundStatement(SELECT_ALL_ROOMS);
        ResultSet rs = null;
        try {
            rs = session.execute(selectAllRooms);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String roomId = row.getUUID("roomId").toString();
            int capacity = row.getInt("capacity");
            rooms.add(new Room(roomId, capacity));
        }
        return rooms;
    }
}