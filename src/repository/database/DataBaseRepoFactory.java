package repository.database;

import domain.validators.Validator;
import exceptions.RepoException;

public class DataBaseRepoFactory implements DataBaseFactory {
    private final DataBaseAcces data;

    public DataBaseRepoFactory(DataBaseAcces data ) {
        this.data = data;
    }

    @Override
    public AbstractDataBaseRepo createRepo(DataBaseStrategy strategy, Validator validator) {
        switch (strategy) {
            case User -> {
                return new UserDataBaseRepo(validator, data, strategy.toString());
            }
            case Friendship -> {
                return new FriendshipDataBaseRepo(validator, data, strategy.toString());
            }
            default -> {
                throw new RepoException("Invalid strategy");
            }
        }
    }
}
