package domain.validators;

import exceptions.ValidationException;
import domain.User;

/**
 * Class for validating a user
 */
public class UserValidator implements Validator<User> {
    /**
     * Method for validating a user
     * @param entity - the user to be validated
     * @throws ValidationException if the user is not valid
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String toThrow;
        toThrow = validateFirstName(entity) + validateLastName(entity) + validateUsername(entity);
        if (toThrow.isEmpty()) {
            return;
        }
        throw new ValidationException(toThrow);
    }

    /**
     * Method for validating the first name of a user
     * @param entity - the user to be validated
     * @return an empty string if the first name is valid, an error message otherwise
     */
    private String validateFirstName(User entity) {
        String toThrow="";
        String firstName = entity.getFirstName();
        if(firstName == null || firstName.trim().isEmpty()) {
            toThrow += "First name is required";
        }
        return toThrow;
    }

    /**
     * Method for validating the last name of a user
     * @param entity - the user to be validated
     * @return an empty string if the last name is valid, an error message otherwise
     */
    private String validateLastName(User entity) {
        String toThrow="";
        String lastName = entity.getLastName();
        if(lastName == null || lastName.trim().isEmpty()) {
            toThrow += "Last name is required";
        }
        return toThrow;
    }

    /**
     * Method for validating the username of a user
     * @param entity - the user to be validated
     * @return an empty string if the username is valid, an error message otherwise
     */
    private String validateUsername(User entity) {
        String toThrow="";
        String username = entity.getUsername();
        if(username == null || username.trim().isEmpty()) {
            toThrow += "Username is required";
        }
        return toThrow;
    }


}
