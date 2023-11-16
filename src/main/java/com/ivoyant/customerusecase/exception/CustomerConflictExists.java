package com.ivoyant.customerusecase.exception;

public class CustomerConflictExists extends CustomerGlobalException {

    private static final long serialVersionUID = 1L;

    public CustomerConflictExists() {
        super(" Customer already exist", GlobalErrorCode.ERROR_RESOURCE_CONFLICT_EXISTS);
    }

    public CustomerConflictExists(String message) {
        super(message, GlobalErrorCode.ERROR_RESOURCE_CONFLICT_EXISTS);
    }
}
