package org.example.bookweb.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(Object password,
                           ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(password)
                .getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(password)
                .getPropertyValue(fieldMatch);

        if (fieldValue != null) {
            return Objects.equals(fieldValue, fieldMatchValue);
        } else {
            return fieldMatchValue == null;
        }
    }
}
