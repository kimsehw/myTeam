package com.kimsehw.myteam.exception;

import com.kimsehw.myteam.domain.FieldError;

public class FieldErrorException extends IllegalArgumentException {

    private FieldError fieldError;

    public FieldErrorException(String message) {
        super(message);
    }

    public FieldErrorException(FieldError fieldError) {
        super();
    }

    public FieldError getFieldError() {
        return fieldError;
    }
}
