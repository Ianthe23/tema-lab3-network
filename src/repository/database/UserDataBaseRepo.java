package repository.database;

import domain.User;
import domain.validators.Validator;
import exceptions.RepoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDataBaseRepo extends AbstractDataBaseRepo<Integer, User> {
    public UserDataBaseRepo (Validator<User> validator, DataBaseAcces data, String Table) {
        super(validator, data, Table);
    }
    private Optional<User> getUser(ResultSet resultSet, Integer id) {
        try {
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String username = resultSet.getString("username");
            User user = new User(firstName, lastName, username);
            user.setId(id);
            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    public Iterable<User> findAll() {
        String sql = "SELECT * FROM \"" + table + "\"";
        Set<User> users = new HashSet<>();
        try {
            PreparedStatement statement = data.createStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                users.add(getUser(resultSet, id).get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null");
        }
        validator.validate(entity);
        String sql = "INSERT INTO \"" + table + "\"" + " (firstname, lastname, username) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = data.createStatement(sql);
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getUsername());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

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
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

}
