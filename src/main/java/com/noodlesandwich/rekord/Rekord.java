package com.noodlesandwich.rekord;

import java.util.Set;

public final class Rekord<T extends RekordType> {
    private final String name;
    private final Properties properties;

    public Rekord(String name, Properties properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T extends RekordType> Rekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T extends RekordType> Rekord<T> create(String name) {
        return new Rekord<>(name, new Properties());
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return key.retrieveFrom(properties);
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

        return new Rekord<>(name, key.storeTo(properties, value));
    }

    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name, properties.without(key));
    }

    @SuppressWarnings("unchecked")
    public <R> R collect(Kollector<T, R> collector) {
        Kollector.Accumulator<T> accumulator = collector.accumulator();
        for (Key<? super T, ?> key : properties.<T>keys()) {
            accumulator.accumulate((Key<? super T, Object>) key, key.retrieveFrom(properties));
        }
        return collector.finish(accumulator);
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
