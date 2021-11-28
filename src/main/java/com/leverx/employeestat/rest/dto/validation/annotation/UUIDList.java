package com.leverx.employeestat.rest.dto.validation.annotation;

import com.leverx.employeestat.rest.dto.validation.validator.UUIDAnnotationValidator;
import com.leverx.employeestat.rest.dto.validation.validator.UUIDListAnnotationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UUIDListAnnotationValidator.class)
public @interface UUIDList {

    public String message() default "The List of UUID contains incorrect UUID value";

    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
