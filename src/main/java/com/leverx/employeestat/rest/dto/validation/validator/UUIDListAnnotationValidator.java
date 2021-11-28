package com.leverx.employeestat.rest.dto.validation.validator;

import com.leverx.employeestat.rest.dto.validation.annotation.UUIDList;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.UUID;

public class UUIDListAnnotationValidator implements ConstraintValidator<UUIDList, List<UUID>> {

    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    @Override
    public void initialize(UUIDList constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<UUID> uuids, ConstraintValidatorContext constraintValidatorContext) {
        if (uuids == null) {
            return true;
        }
        for (UUID id : uuids) {
            if (!id.toString().matches(UUID_PATTERN)) {
                return false;
            }
        }
        return true;
    }
}
