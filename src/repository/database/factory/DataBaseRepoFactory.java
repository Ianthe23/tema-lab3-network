package repository.database.factory;

import domain.validators.Validator;
import exceptions.RepoException;
import repository.database.*;
import repository.database.utils.AbstractDataBaseRepo;
import repository.database.utils.DataBaseAcces;

/**
 * Factory for creating database repositories
 */
public class DataBaseRepoFactory implements DataBaseFactory {
    private final DataBaseAcces data;

    /**
     * Constructor
     * @param data - the database access
     */
    public DataBaseRepoFactory(DataBaseAcces data ) {
        this.data = data;
    }

    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
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
