package trainingsapp.backend;

import java.util.UUID;

class Room {
    public String roomId;
    public int capacity;

    public Room(int capacity) {
        this.roomId = UUID.randomUUID().toString();
        this.capacity = capacity;
    }

    public Room(String roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
    }

    public boolean equals(Room room) {
        return this.roomId.equals(room.roomId) &&
               this.capacity == room.capacity;
    }
}