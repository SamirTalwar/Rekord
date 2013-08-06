package com.noodlesandwich.rekord;

import java.util.Arrays;
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

    public <V> V get(Key<? super T, V> key) {
        return key.transform(properties.get(key));
    }

    public boolean containsKey(Key<T, ?> key) {
        return properties.contains(key);
    }

    public Set<Key<? super T, ?>> keys() {
        return properties.keys();
    }

    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        return with(key.of(value));
    }

    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    public <V> Rekord<T> with(Property<? super T, V> property) {
        return new Rekord<>(name, this.properties.with(property));
    }

    @SafeVarargs
    public final <V> Rekord<T> with(Property<? super T, V>... properties) {
        return with(Arrays.asList(properties));
    }

    public <V> Rekord<T> with(Iterable<Property<? super T, V>> properties) {
        Rekord<T> intermediateRekord = this;
        for (Property<? super T, V> property : properties) {
            intermediateRekord = with(property);
        }
        return intermediateRekord;
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
