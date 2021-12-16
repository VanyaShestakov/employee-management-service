package com.leverx.employeestat.rest.controller.tool;

import com.leverx.employeestat.rest.controller.AuthorizationController;
import com.leverx.employeestat.rest.exception.NotValidUUIDException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;

public class UUIDUtils {

    private final static Logger log = LogManager.getLogger(UUIDUtils.class);

    public static UUID getUUIDFromString(String id) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            NotValidUUIDException ex = new NotValidUUIDException(String.format("Value =%s is not UUID", id), e);
            log.error("Thrown exception");
            throw ex;
        }
        return uuid;
    }
}
