package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class RenamedKey<T, V> extends DelegatingKey<T, V> {
    private final Key<T, V> key;

    public RenamedKey(String name, Key<T, V> key) {
        super(name, key);
        this.key = key;
    }

    public static <T, V> UnnamedRenamedKey<T, V> renaming(Key<T, V> key) {
        return new UnnamedRenamedKey<>(key);
    }

    @Override
    public <R extends T> boolean test(PropertyMap<R> properties) {
        return key.test(properties);
    }

    @Override
    public <R extends T> V get(PropertyMap<R> properties) {
        return key.get(properties);
    }

    @Override
    public <R extends T> PropertyMap<R> set(V value, PropertyMap<R> properties) {
        return key.set(value, properties);
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    public static final class UnnamedRenamedKey<T, V> {
        private final Key<T, V> key;

        public UnnamedRenamedKey(Key<T, V> key) {
            this.key = key;
        }

        public Key<T, V> to(String name) {
            return new RenamedKey<>(name, key);
        }
    }
}
