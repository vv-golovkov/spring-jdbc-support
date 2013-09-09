package com.ericsson.springsupport.jdbc.exception;

public class IncorrectResultDataException extends RuntimeException {
    private static final long serialVersionUID = 9077671031193283688L;

    public IncorrectResultDataException() {
    }

    public IncorrectResultDataException(String message, Throwable t) {
        super(message, t);
    }

    public IncorrectResultDataException(Throwable t) {
        super(t);
    }

    public IncorrectResultDataException(String message) {
        super(message);
    }
}
