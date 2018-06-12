package nurteen.prometheus.pc.framework;



import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

public class Argument {
    static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public void validate() throws InvalidArgumentException {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(this);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            this.buildMessage(message, violations);
            throw new InvalidArgumentException(message.toString());
        }
    }

    private void buildMessage(StringBuilder message, Set<ConstraintViolation<Object>> violations) {
        Iterator<ConstraintViolation<Object>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            message.append(iterator.next().getMessage());

            if (iterator.hasNext()) {
                message.append("; ");
            }
        }
    }
}
