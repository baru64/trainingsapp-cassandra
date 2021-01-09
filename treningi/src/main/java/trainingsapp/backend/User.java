package trainingsapp.backend;

import java.util.UUID;

class User {
    public String userId;
    public String name;
    public int phone;

    public User(String name, int phone) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
    }

    public User(String userId, String name, int phone) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
    }

    public boolean equals(User user) {
        return this.name.equals(user.name) &&
               this.userId.equals(user.userId) &&
               this.phone == user.phone;
    }
}