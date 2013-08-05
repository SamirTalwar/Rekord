package com.noodlesandwich.rekord;

import java.util.Map;
import com.noodlesandwich.rekord.keys.OrdinaryKey;

public abstract class Key<T, V> {
    public static <T, V> Key<T, V> named(String name) {
        return new OrdinaryKey<>(name);
    }

    @SuppressWarnings("unchecked")
    public abstract V retrieveFrom(Map<Key<? super T, ?>, Object> properties);

    public abstract boolean isContainedIn(Map<Key<? super T, ?>, Object> properties);
}
