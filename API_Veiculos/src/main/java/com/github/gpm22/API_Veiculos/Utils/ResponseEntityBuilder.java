package com.github.gpm22.API_Veiculos.Utils;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResponseEntityBuilder {

    public static ResponseStatusException buildErrorResponse(Exception e, HttpStatus httpStatus, String errorMessage,
            Logger logger) {
        logger.error(errorMessage);
        return new ResponseStatusException(httpStatus, e.getMessage());
    }
}
