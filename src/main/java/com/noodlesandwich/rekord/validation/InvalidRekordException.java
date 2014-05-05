package com.noodlesandwich.rekord.validation;

public class InvalidRekordException extends Exception {
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
