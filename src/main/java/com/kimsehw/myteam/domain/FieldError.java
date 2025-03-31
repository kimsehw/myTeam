package com.kimsehw.myteam.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FieldError {

    private String field;
    private String errorMessage;

    private FieldError(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public static FieldError of(String field, String errorMessage) {
        return new FieldError(field, errorMessage);
    }
}
