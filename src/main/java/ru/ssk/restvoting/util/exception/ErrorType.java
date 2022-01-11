package ru.ssk.restvoting.util.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    DATA_NOT_FOUND("error.dataNotFound", HttpStatus.UNPROCESSABLE_ENTITY),
    VALIDATION_ERROR("error.validationError", HttpStatus.UNPROCESSABLE_ENTITY),
    DATA_ERROR("error.dataError", HttpStatus.CONFLICT),
    WRONG_REQUEST("error.wrongRequest", HttpStatus.BAD_REQUEST),
    APP_ERROR("error.appError", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final HttpStatus status;

    ErrorType(String errorCode, HttpStatus status) {
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
