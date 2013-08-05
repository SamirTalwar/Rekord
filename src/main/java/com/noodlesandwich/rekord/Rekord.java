package com.noodlesandwich.rekord;

import java.util.Set;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class Rekord<T extends RekordType> {
    private final String name;
    private final PMap<Key<? super T, ?>, Object> properties;

    public Rekord(String name, PMap<Key<? super T, ?>, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T extends RekordType> Rekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T extends RekordType> Rekord<T> create(String name) {
        return new Rekord<>(name, HashTreePMap.<Key<? super T, ?>, Object>empty());
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    public Set<Key<? super T, ?>> keys() {
        return properties.keySet();
    }

    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        return new Rekord<>(name, properties.plus(key, value));
    }

    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name, properties.minus(key));
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
