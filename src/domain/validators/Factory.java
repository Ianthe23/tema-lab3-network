package domain.validators;

/**
 * Interface for a factory
 */
public interface Factory {
    /**
     * Method for creating a validator
     * @param strategy - the strategy for the validator
     * @return a new validator
     */
    Validator createValidator(ValidatorStrategy strategy);
}
