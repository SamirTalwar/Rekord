package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

public final class RenamedKey<T, V> extends DelegatingKey<T, V> {
    private final Key<T, V> key;

    private RenamedKey(String name, Key<T, V> key) {
        super(name, key);
        this.key = key;
    }

    public static <T, V> UnnamedRenamedKey<T, V> renaming(Key<T, V> key) {
        return new UnnamedRenamedKey<>(key);
    }

    @Override
    public <R extends T> boolean test(Properties<R> properties) {
        return key.test(properties);
    }

    @Override
    public <R extends T> V get(Properties<R> properties) {
        return key.get(properties);
    }

    @Override
    public <R extends T> Properties<R> set(V value, Properties<R> properties) {
        return key.set(value, properties);
    }

    public static final class UnnamedRenamedKey<T, V> {
        private final Key<T, V> key;

        private UnnamedRenamedKey(Key<T, V> key) {
            this.key = key;
        }

        public Key<T, V> to(String name) {
            return new RenamedKey<>(name, key);
        }
    }
}
