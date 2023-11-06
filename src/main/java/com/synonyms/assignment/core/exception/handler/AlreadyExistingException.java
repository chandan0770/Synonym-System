package com.synonyms.assignment.core.exception.handler;

import lombok.Getter;

/**
 * AlreadyExistingException is a generic RunTimeException which is thrown whenever a type is already
 * exists. It has a property of message which is set using constructor.
 */
@Getter
public class AlreadyExistingException extends RuntimeException {

    private String error;

    public AlreadyExistingException(String message) {
        this.error = message;
    }
}
