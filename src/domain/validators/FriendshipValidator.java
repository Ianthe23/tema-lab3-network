package domain.validators;

import exceptions.ValidationException;
import domain.Friendship;

import java.util.Objects;

/**
 * Class for a friendship validator
 */
public class FriendshipValidator implements Validator<Friendship> {
    /**
     * Method for validating a friendship
     * @param entity - the friendship to be validated
     * @throws ValidationException if the friendship is not valid
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String toThrow="";

        if (entity.getSince() == null) {
            toThrow += "Date is null";
        }

        if (entity.getId().getFirst() == null || entity.getId().getSecond() == null) {
            toThrow += "Id is null";
        }

        if (Objects.equals(entity.getId().getFirst(), entity.getId().getSecond())) {
            toThrow += "Id is the same";
        }

        if (!toThrow.isEmpty()) {
            throw new ValidationException(toThrow);
        }
    }
}
