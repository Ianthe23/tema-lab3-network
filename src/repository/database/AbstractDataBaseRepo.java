package repository.database;

import domain.Entity;
import domain.validators.Validator;
import repository.Repository;

import java.util.Optional;
/**
 * Abstract class for a database repository
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public abstract class AbstractDataBaseRepo <ID, E extends Entity<ID>> implements Repository<ID, E>{
    protected Validator validator;
    protected DataBaseAcces data;
    protected String table;

    /**
     * Constructor
     * @param validator - the validator
     * @param data - the database access
     * @param table - the table name
     */
    public AbstractDataBaseRepo(Validator validator, DataBaseAcces data, String table) {
        this.validator = validator;
        this.data = data;
        this.table = table;
    }

    /**
     * Constructor
     */
    public AbstractDataBaseRepo() {
        super();
    }
}
