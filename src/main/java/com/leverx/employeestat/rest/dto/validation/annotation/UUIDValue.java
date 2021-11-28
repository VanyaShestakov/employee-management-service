package com.leverx.employeestat.rest.dto.validation.annotation;

import com.leverx.employeestat.rest.dto.validation.validator.UUIDAnnotationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UUIDAnnotationValidator.class)
public @interface UUIDValue {

    public String message() default "The UUID is not correct";

    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
