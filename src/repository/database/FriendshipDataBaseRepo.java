package repository.database;

import domain.Entity;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validators.Validator;
import exceptions.RepoException;
import repository.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDataBaseRepo extends AbstractDataBaseRepo<Tuple<Integer, Integer>, Friendship> {
    public FriendshipDataBaseRepo(Validator validator, DataBaseAcces data, String table) {
        super(validator, data, table);
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        String insertSQL = "INSERT INTO \"" + table + "\"" + " (user_id1, user_id2) VALUES (?, ?)";
        try {
            PreparedStatement statement = data.createStatement(insertSQL);
            statement.setInt(1, entity.getId().getFirst());
            statement.setInt(2, entity.getId().getSecond());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RepoException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<Integer, Integer> id) {
        Optional<Friendship> entity = findOne(id);

        if (id != null) {
            String deleteStatement  = "DELETE FROM \"" + table + "\"" + " WHERE user_id1 = ? AND user_id2 = ?";
            int response = 0;
            try {
                PreparedStatement statement = data.createStatement(deleteStatement);
                statement.setInt(1, id.getFirst());
                statement.setInt(2, id.getSecond());
                if (entity.isPresent()) {
                    response = statement.executeUpdate();
                }
                return response == 0 ? Optional.empty() : entity;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            throw new IllegalArgumentException("ID must not be null");
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null) {
            throw new RepoException("Entity must not be null");
        }

        String updateStatement = "UPDATE \"" + table + "\"" + " SET user_id1 = ?, user_id2 = ? WHERE (user_id1 = ? AND user_id2 = ?) OR (user_id2 = ? AND user_id1 = ?)";

        try {
            PreparedStatement statement = data.createStatement(updateStatement);
            statement.setInt(1, entity.getId().getFirst());
            statement.setInt(2, entity.getId().getSecond());
            statement.setInt(3, entity.getId().getFirst());
            statement.setInt(4, entity.getId().getSecond());
            statement.setInt(5, entity.getId().getSecond());
            statement.setInt(6, entity.getId().getFirst());
            int response = statement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Integer, Integer> id) {
        if (id == null) {
            throw new IllegalArgumentException("ID must not be null");
        }

        String sql = "SELECT * FROM \"" + table + "\"" +
                " WHERE (id1 = ? AND id2 = ?) OR (id2 = ? AND id1 = ?)";
        try {
            PreparedStatement statement = data.createStatement(sql);
            statement.setInt(1, id.getFirst());
            statement.setInt(2, id.getSecond());
            statement.setInt(3, id.getFirst());
            statement.setInt(4, id.getSecond());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Friendship friendship = getFriendship(resultSet);
                return Optional.of(friendship);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Friendship getFriendship(ResultSet resultSet) throws SQLException {
        Integer id1 = resultSet.getInt("user1_id");
        Integer id2 = resultSet.getInt("user2_id");
        String firstName1 = resultSet.getString("firstName1");
        String lastName1 = resultSet.getString("lastName1");
        String username1 = resultSet.getString("username1");
        String firstName2 = resultSet.getString("firstName2");
        String lastName2 = resultSet.getString("lastName2");
        String username2 = resultSet.getString("username2");

        User user1 = new User(firstName1, lastName1, username1);
        user1.setId(id1);
        User user2 = new User(firstName2, lastName2, username2);
        user2.setId(id2);

        return new Friendship(user1, user2);
    }



    @Override
    public Iterable<Friendship> findAll() {
        String findAllStatement = "SELECT * FROM \"" + table + "\"";
        Set<Friendship> friendships = new HashSet<>();

        try {
            PreparedStatement statement = data.createStatement(findAllStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getFriendship(resultSet);
                friendships.add(friendship);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships;
    }
}
