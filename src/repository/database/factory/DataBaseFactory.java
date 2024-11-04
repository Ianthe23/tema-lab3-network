package repository.database.factory;

import domain.validators.Validator;
import repository.database.utils.AbstractDataBaseRepo;

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
