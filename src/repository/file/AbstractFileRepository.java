package repository.file;

import domain.Entity;
import domain.validators.Validator;
import repository.memory.InMemoryRepository;

import java.io.*;

/**
 * CRUD operations repository for file storage
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E>{
    private String filename;

    /**
     * Constructor
     * @param validator - the validator for the entities
     * @param fileName - the name of the file where the data is stored
     */
    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename=fileName;
        loadData();
    }

    /**
     * Abstract method for reading a line from the file and converting it to an entity
     * @param line - the line to be converted
     * @return the entity obtained from the line
     */
    public abstract E lineToEntity(String line);

    /**
     * Abstract method for converting an entity to a line
     * @param entity - the entity to be converted
     * @return the line obtained from the entity
     */
    public abstract String entityToLine(E entity);

    /**
     * Method for saving an entity
     * @param entity - the entity to be saved
     * @return null - if the entity is saved,
     *         the entity - otherwise
     */
    @Override
    public E save(E entity) {
        E e = super.save(entity);
        if (e == null)
            writeToFile();
        return e;
    }

    /**
     * Method for writing the entities to the file
     */
    private void writeToFile() {

        try  ( BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (E entity: entities.values()) {
                String ent = entityToLine(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method for deleting an entity
     * @param id - the id of the entity to be deleted
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if (e !=  null) {
            writeToFile();
        }
        return e;
    }

    /**
     * Method for updating an entity
     * @param entity - the entity to be updated
     * @return null - if the entity is updated,
     *         the entity - otherwise
     */
    @Override
    public E update(E entity) {
        E e = super.update(entity);
        if (e == null) {
            writeToFile();
        }
        return e;
    }

    /**
     * Method for loading the data from the file
     */
    private void loadData() {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while( (line = reader.readLine()) != null) {
                E entity = lineToEntity(line);
                entities.put(entity.getId(), entity);
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for appending an entity to the file
     * @param entity - the entity to be appended
     */
    private void appendToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String ent = entityToLine(entity);
            writer.write(ent);
            writer.newLine();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
