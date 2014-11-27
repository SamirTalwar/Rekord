package com.noodlesandwich.rekord.keys;

public final class CopiedKey<T, V> extends ForwardingKey<T, V> {
    @SuppressWarnings("unchecked")
    public <U> CopiedKey(Key<U, V> key) {
        super(key.name(), (Key<T, V>) key);
    }

    public static <T, U, V> Key<T, V> from(Key<U, V> key) {
        return new CopiedKey<>(key);
    }
}
