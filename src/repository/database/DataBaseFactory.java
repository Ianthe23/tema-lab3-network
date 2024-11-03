package repository.database;

import domain.validators.Validator;

/**
 * Interface for a database factory
 */
public interface DataBaseFactory {
    /**
     * Method to create a repository
     * @param strategy - the strategy
     * @param validator - the validator
     * @return AbstractDataBaseRepo
     */
    AbstractDataBaseRepo createRepo(DataBaseStrategy strategy, Validator validator);
}
