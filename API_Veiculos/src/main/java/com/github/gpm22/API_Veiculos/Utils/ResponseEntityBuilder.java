package com.github.gpm22.API_Veiculos.Utils;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    public static ResponseEntity<String> buildErrorResponse(Exception e, HttpStatus httpStatus, String errorMessage, Logger logger){
        logger.error(errorMessage);
        e.printStackTrace();
        return ResponseEntity
                .status(httpStatus)
                .body(e.getMessage());
    }
}
