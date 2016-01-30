package com.noodlesandwich.rekord.keys;

public final class KeyNotFoundException extends Exception {
    private static final long serialVersionUID = -7610185085730356265L;
    public static final String NonExistentKeyTemplate = "The key \"%s\" does not exist.";

    public KeyNotFoundException(String name) {
        super(String.format(NonExistentKeyTemplate, name));
    }
}
