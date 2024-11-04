package repository.database;

import domain.User;
import domain.validators.Validator;
import exceptions.RepoException;
import repository.database.utils.AbstractDataBaseRepo;
import repository.database.utils.DataBaseAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Database repository for users
 */
public class UserDataBaseRepo extends AbstractDataBaseRepo<Integer, User> {
    /**
     * Constructor
     * @param validator - the validator
     * @param data - the database access
     * @param Table - the table name
     */
    public UserDataBaseRepo (Validator<User> validator, DataBaseAcces data, String Table) {
        super(validator, data, Table);
    }

    /**
     * Method to get a user from a result set
     * @param resultSet - the result set
     * @param id - the user id
     * @return an {@code Optional} - null if the user was not found, the user otherwise
     */
    private Optional<User> getUser(ResultSet resultSet, Integer id) {
        try {
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String username = resultSet.getString("username");
            User user = new User(firstName, lastName, username);
            user.setId(id);
            user.setFriendships(new ArrayList<>()); // Initialize an empty list for friendships
            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to load friendships
     * @param userMap - the user map
     */
    private void loadFriendships(Map<Integer, User> userMap) {
        String friendshipSql = "SELECT * FROM \"Friendship\"";

        try (PreparedStatement statement = data.createStatement(friendshipSql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int user1Id = resultSet.getInt("user1_id");
                int user2Id = resultSet.getInt("user2_id");

                User user1 = userMap.get(user1Id);
                User user2 = userMap.get(user2Id);

                if (user1 != null && user2 != null) {
                    user1.getFriendships().add(user2);
                    user2.getFriendships().add(user1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to find a user by id
     * @param id - the user id
     * @return an {@code Optional} - null if the user was not found, the user otherwise
     */
    @Override
    public Optional<User> findOne(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String sql = "SELECT * FROM \"" + table + "\"" + " WHERE id = ?";

        try
        {
            PreparedStatement statement=data.createStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()) {
                return getUser(resultSet,id);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to find all users
     * @return an {@code Iterable} - the users
     */
    @Override
    public Iterable<User> findAll() {
        String sql = "SELECT * FROM \"User\"";
        Map<Integer, User> userMap = new HashMap<>();

        try (PreparedStatement statement = data.createStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Optional<User> optionalUser = getUser(resultSet, id);
                optionalUser.ifPresent(user -> userMap.put(id, user));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Load all friendships
        loadFriendships(userMap);

        return userMap.values();
    }

    /**
     * Method to check if a user exists
     * @param username - the username
     * @return a boolean - true if the user exists, false otherwise
     */
    private boolean userExists(String username) {
        String checkUserSql = "SELECT COUNT(*) FROM \"User\" WHERE username = ?";

        try (PreparedStatement statement = data.createStatement(checkUserSql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to save a user
     * @param entity - the user to save
     * @return an {@code Optional} - null if the user was saved, the user otherwise
     */
    @Override
    public Optional<User> save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }

        validator.validate(entity);

        // Check if the user already exists
        if (userExists(entity.getUsername())) {
            throw new RepoException("User already exists");
        }

        String sql = "INSERT INTO \"User\" (firstname, lastname, username) VALUES (?, ?, ?)";

        try (PreparedStatement statement = data.createStatement(sql)) {
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());

            int response = statement.executeUpdate();
            return response > 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    /**
     * Method to delete a user
     * @param id - the user id
     * @return an {@code Optional} - null if the user was deleted, the user otherwise
     */
    @Override
    public Optional<User> delete(Integer id) {
        Optional<User> entity = findOne(id);
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }
        String sql = "DELETE FROM \"" + table + "\"" + " WHERE id = ?";
        int response = 0;
        try {
            PreparedStatement statement = data.createStatement(sql);
            if (entity.isPresent()) {
                statement.setInt(1, id);
                response = statement.executeUpdate();
            }
            return response == 0 ? Optional.empty() : entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to update a user
     * @param entity - the user to update
     * @return an {@code Optional} - null if the user was updated, the user otherwise
     */
    @Override
    public Optional<User> update(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        String sql = "UPDATE \"" + table + "\"" + " SET firstname = ?, lastname = ?, username = ? WHERE id = ?";
        try {
            PreparedStatement statement = data.createStatement(sql);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            statement.setInt(4, entity.getId());
            int response = statement.executeUpdate();
            return response == 1 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

}
