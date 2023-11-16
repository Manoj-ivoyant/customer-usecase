package com.ivoyant.customerusecase.exception;

public class CustomerNotFound extends CustomerGlobalException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CustomerNotFound() {
        super("customer not found", GlobalErrorCode.ERROR_RESOURCE_NOT_FOUND);
    }

    public CustomerNotFound(String message) {
        super(message, GlobalErrorCode.ERROR_RESOURCE_NOT_FOUND);
    }

}
