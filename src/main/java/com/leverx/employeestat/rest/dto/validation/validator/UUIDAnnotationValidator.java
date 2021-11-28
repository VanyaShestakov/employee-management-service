package com.leverx.employeestat.rest.dto.validation.validator;

import com.leverx.employeestat.rest.dto.validation.annotation.UUIDValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

public class UUIDAnnotationValidator implements ConstraintValidator<UUIDValue, UUID> {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public void initialize(UUIDValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        if (uuid == null) {
            return true;
        }
        return uuid.toString().matches(UUID_PATTERN);
    }
}
