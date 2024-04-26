package org.example.bookweb.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, String> {

    private String field;
    private String fieldMatch;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
    }

    @Override
    public boolean isValid(String password,
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
