package repository.database;

import domain.validators.Validator;

public interface DataBaseFactory {
    AbstractDataBaseRepo createRepo(DataBaseStrategy strategy, Validator validator);
}
