package repository.database;

import domain.Entity;
import domain.validators.Validator;
import repository.Repository;

import java.util.Optional;

public abstract class AbstractDataBaseRepo <ID, E extends Entity<ID>> implements Repository<ID, E>{
    protected Validator validator;
    protected DataBaseAcces data;
    protected String table;

    public AbstractDataBaseRepo(Validator validator, DataBaseAcces data, String table) {
        this.validator = validator;
        this.data = data;
        this.table = table;
    }

    public AbstractDataBaseRepo() {
        super();
    }
}
