package trainingsapp.backend;

import java.util.LinkedList;

import com.datastax.driver.core.*;

public class UserController {

    private final Session session;

    private static PreparedStatement SELECT_ALL_USERS;
    private static PreparedStatement SELECT_USER_BY_ID;
    private static PreparedStatement SELECT_USER_BY_NAME;
    private static PreparedStatement INSERT_USER;
    private static PreparedStatement DELETE_USER;


    public UserController(Session session) throws BackendException {
        this.session = session;

        prepareStatements();
    }

    private void prepareStatements() throws BackendException {
        try {
            SELECT_ALL_USERS = session.prepare("SELECT * FROM users;");
            SELECT_USER_BY_ID = session.prepare("SELECT * FROM users WHERE name=? AND userId=?;");
            SELECT_USER_BY_NAME = session.prepare("SELECT * FROM users WHERE name=?;");
            INSERT_USER = session.prepare("INSERT INTO users (userId, name, phone) VALUES (?,?,?);");
            DELETE_USER = session.prepare("DELETE FROM users WHERE userId=?;");
        } catch (Exception e) {
            throw new BackendException("Could not prepare statements" + e.getMessage() + ".", e);
        }
    }

    public User createUser(String name, int phone) throws BackendException {
        BoundStatement insertUser = new BoundStatement(INSERT_USER);
        BoundStatement selectUser = new BoundStatement(SELECT_USER_BY_NAME);
        ResultSet rs = null;
        User user = new User(name, phone);
        try {
            selectUser.bind(user.name);
            rs = session.execute(selectUser);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
        if (rs.iterator().hasNext()) {
            throw new BackendException("Users with this phone number exists.");
        } else {
            try {
                insertUser.bind(user.userId, user.name, user.phone);
                session.execute(insertUser);
            } catch (Exception e) {
                throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
            }
        }
        return user;
    }

    public void deleteUser(String userId) throws BackendException {
        BoundStatement deleteUser = new BoundStatement(DELETE_USER);
        try {
            deleteUser.bind(userId);
            session.execute(deleteUser);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }
    }

    public User selectUserById(String userId, String userName) throws BackendException {
        BoundStatement selectUser = new BoundStatement(SELECT_USER_BY_ID);
        ResultSet rs = null;

        try {
            selectUser.bind(userName, userId);
            rs = session.execute(selectUser);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        String name = rs.one().getString("name");
        int phone = rs.one().getInt("phone");
        return new User(userId, name, phone);
    }

    public LinkedList<User> selectAllUsers() throws BackendException {
        LinkedList<User> users = new LinkedList<>();
        BoundStatement selectAllUsers = new BoundStatement(SELECT_ALL_USERS);
        ResultSet rs = null;
        try {
            rs = session.execute(selectAllUsers);
        } catch (Exception e) {
            throw new BackendException("Could not perform a query. " + e.getMessage() + ".", e);
        }

        for (Row row : rs) {
            String userId = row.getUUID("userId").toString();
            String name = row.getString("name");
            int phone = row.getInt("phone");

            users.add(new User(userId, name, phone));
        }
        return users;
    }
}