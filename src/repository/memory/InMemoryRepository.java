package repository.memory;


import domain.Entity;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * CRUD operations repository for in-memory storage
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    public Map<ID, E> entities;

    /**
     * Constructor
     * @param validator - the validator for the entities
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    /**
     * Method for finding an entity
     * @param id - the id of the entity to be found
     * @return the entity with the given id
     */
    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("ID must be not null");
        return entities.get(id);
    }

    /**
     * Method for finding all entities
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * Method for saving an entity
     * @param entity - the entity to be saved
     * @return null - if the entity is saved,
     *         the entity - otherwise
     */
    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        E oldEntity = entities.get(entity.getId());
        if (oldEntity != null) {
            return oldEntity;
        } else {
            entities.put(entity.getId(), entity);
            return null;
        }
    }

    /**
     * Method for deleting an entity
     * @param id - the id of the entity to be deleted
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("ID must be not null");
        return entities.remove(id);
    }

    /**
     * Method for updating an entity
     * @param entity - the entity to be updated
     * @return null - if the entity is updated,
     *         the entity - otherwise
     */
    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null!");
        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return null;
        }
        return entity;
    }
}
