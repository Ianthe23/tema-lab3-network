package repository.file;

import domain.User;
import domain.validators.Validator;

/**
 * CRUD operations repository for file storage
 */
public class UserRepository extends AbstractFileRepository<Integer, User>{
    /**
     * Constructor
     * @param validator - the validator for the entities
     * @param fileName - the name of the file where the data is stored
     */
    public UserRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * Method for reading a line from the file and converting it to an entity
     * @param line - the line to be converted
     * @return the entity obtained from the line
     */
    @Override
    public User lineToEntity(String line) {
        String[] splited = line.split(";");
        User u = new User(splited[1], splited[2], splited[3]);
        u.setId(Integer.parseInt(splited[0]));
        return u;
    }

    /**
     * Method for converting an entity to a line
     * @param entity - the entity to be converted
     * @return the line obtained from the entity
     */
    @Override
    public String entityToLine(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getUsername();
    }
}
