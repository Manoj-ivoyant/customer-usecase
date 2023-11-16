package com.ivoyant.customerusecase.exception;

import org.springframework.http.HttpStatus;

public class GlobalErrorCode {
    public static final HttpStatus ERROR_RESOURCE_NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus ERROR_RESOURCE_CONFLICT_EXISTS = HttpStatus.CONFLICT;
}
