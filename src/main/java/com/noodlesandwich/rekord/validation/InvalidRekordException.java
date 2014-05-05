package com.noodlesandwich.rekord.validation;

public class InvalidRekordException extends Exception {
    public InvalidRekordException() {
        super("This rekord is invalid.");
    }
}
