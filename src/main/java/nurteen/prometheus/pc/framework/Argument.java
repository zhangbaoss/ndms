package nurteen.prometheus.pc.framework;

import nurteen.prometheus.pc.framework.exception.InvalidArgumentException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
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

    public static ValidateHelper notNull(Object argument, String message) {
        return new ValidateHelper().notNull(argument, message);
    }

    public ValidateHelper notEmpty(String argument, String message) {
        return new ValidateHelper().notEmpty(argument, message);
    }

    public <T> ValidateHelper notEmpty(Collection<T> argument, String message) {
        return new ValidateHelper().notEmpty(argument, message);
    }

    public <K, V> ValidateHelper notEmpty(Map<K, V> argument, String message) {
        return new ValidateHelper().notEmpty(argument, message);
    }

    public ValidateHelper startsWith(String argument, String prefix, String message) {
        return new ValidateHelper().startsWith(argument, prefix, message);
    }

    public ValidateHelper endsWith(String argument, String suffix, String message) {
        return new ValidateHelper().endsWith(argument, suffix, message);
    }

    public static ValidateHelper length(String argument, int min, int max, String message) {
        return new ValidateHelper().length(argument, min, max, message);
    }

    public static <T> ValidateHelper length(Collection<T> argument, int min, int max, String message) {
        return new ValidateHelper().length(argument, min, max, message);
    }

    public static <K, V> ValidateHelper length(Map<K, V> argument, int min, int max, String message) {
        return new ValidateHelper().length(argument, min, max, message);
    }

    public static <T> ValidateHelper range(Comparable<T> argument, T min, T max, String message) {
        return new ValidateHelper().range(argument, min, max, message);
    }

    public <T> ValidateHelper set(T argument, T[] values, String message) {
        return new ValidateHelper().set(argument, values, message);
    }

    public <T> ValidateHelper set(T argument, Collection<T> values, String message) {
        return new ValidateHelper().set(argument, values, message);
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

    public static class ValidateHelper {
        Integer count = 0;
        StringBuilder builder = new StringBuilder();

        ValidateHelper() {
        }

        private void append(String message) {
            if (this.count > 0) {
                this.builder.append("; ");
            }

            this.builder.append(message);
            ++this.count;
        }

        private <T> boolean contains(T[] values, T value) {
            for (T tmp : values) {
                if (tmp.equals(value)) {
                    return true;
                }
            }
            return false;
        }

        public ValidateHelper notNull(Object argument, String message) {
            if (argument == null) {
                this.append(message);
            }
            return this;
        }

        public ValidateHelper notEmpty(String argument, String message) {
            if (argument.isEmpty()) {
                this.append(message);
            }
            return this;
        }

        public <T> ValidateHelper notEmpty(Collection<T> argument, String message) {
            if (argument.isEmpty()) {
                this.append(message);
            }
            return this;
        }

        public <K, V> ValidateHelper notEmpty(Map<K, V> argument, String message) {
            if (argument.isEmpty()) {
                this.append(message);
            }
            return this;
        }

        public ValidateHelper startsWith(String argument, String prefix, String message) {
            if (!argument.startsWith(prefix)) {
                this.append(message);
            }
            return this;
        }

        public ValidateHelper endsWith(String argument, String suffix, String message) {
            if (!argument.endsWith(suffix)) {
                this.append(message);
            }
            return this;
        }

        public ValidateHelper length(String argument, int min, int max, String message) {
            if ((argument.length() < min) || (argument.length() > max)) {
                this.append(message);
            }
            return this;
        }

        public <T> ValidateHelper length(Collection<T> argument, int min, int max, String message) {
            if ((argument.size() < min) || (argument.size() > max)) {
                this.append(message);
            }
            return this;
        }

        public <K, V> ValidateHelper length(Map<K, V> argument, int min, int max, String message) {
            if ((argument.size() < min) || (argument.size() > max)) {
                this.append(message);
            }
            return this;
        }

        public <T> ValidateHelper range(Comparable<T> argument, T min, T max, String message) {
            if ((argument.compareTo(min) < 0) || (argument.compareTo(max) > 0)) {
                this.append(message);
            }
            return this;
        }

        public <T> ValidateHelper set(T argument, T[] values, String message) {
            if (!this.contains(values, argument)) {
                this.append(message);
            }
            return this;
        }

        public <T> ValidateHelper set(T argument, Collection<T> values, String message) {
            if (!values.contains(argument)) {
                this.append(message);
            }
            return this;
        }

        public void validate() throws InvalidArgumentException {
            if (this.count > 0) {
                throw new InvalidArgumentException(this.builder.toString());
            }
        }
    }
}
