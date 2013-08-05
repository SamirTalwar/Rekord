package com.noodlesandwich.rekord;

import java.util.Set;

public final class Rekord<T extends RekordType> {
    private final String name;
    private final Properties<T> properties;

    public Rekord(String name, Properties<T> properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T extends RekordType> Rekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T extends RekordType> Rekord<T> create(String name) {
        return new Rekord<>(name, new Properties<T>());
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return ((Key<T, V>) key).retrieveFrom(properties);
    }

    public boolean containsKey(Key<T, ?> key) {
        return properties.contains(key);
    }

    public Set<Key<? super T, ?>> keys() {
        return properties.keys();
    }

    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        return new Rekord<>(name, properties.with(key, value));
    }

    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name, properties.without(key));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (!(o instanceof Rekord)) {
            return false;
        }

        return properties.equals(((Rekord<T>) o).properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return name + properties.toString();
    }
}
