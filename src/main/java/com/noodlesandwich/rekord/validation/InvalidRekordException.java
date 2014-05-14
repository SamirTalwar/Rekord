package com.noodlesandwich.rekord.validation;

public class InvalidRekordException extends Exception {
    private static final long serialVersionUID = -8453084058922683226L;

    public InvalidRekordException() {
        super();
    }

    public InvalidRekordException(String message) {
        super(message);
    }

    public InvalidRekordException(Throwable cause) {
        super(cause);
    }

    public InvalidRekordException(String message, Throwable cause) {
        super(message, cause);
    }
}
