package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public final class UnacceptableKeyException extends RuntimeException {
    private static final long serialVersionUID = 4749255838657529314L;

    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    public <T, V> UnacceptableKeyException(Key<? super T, V> key) {
        super(String.format(UnacceptableKeyTemplate, key.name()));
    }
}
