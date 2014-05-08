package com.noodlesandwich.rekord;

import java.util.Arrays;
import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Rekord<T> implements RekordBuilder<T, Rekord<T>>, FixedRekord<T> {
    private final String name;
    private final Properties<T> properties;

    public Rekord(String name, Properties<T> properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <V> V get(Key<? super T, V> key) {
        return key.retrieveFrom(properties);
    }

    @Override
    public boolean containsKey(Key<T, ?> key) {
        return properties.contains(key);
    }

    @Override
    public PSet<Key<? super T, ?>> keys() {
        return properties.keys();
    }

    @Override
    public PSet<Key<? super T, ?>> acceptedKeys() {
        return properties.acceptedKeys();
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        return new Rekord<>(name, key.storeTo(properties, value));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name, properties.without(key));
    }

    @Override
    public <A, R> R serialize(RekordSerializer<A, R> serializer) {
        RekordSerializer.Serializer<A> internalSerializer = serializer.start(name);
        accumulateIn(internalSerializer);
        return serializer.finish(internalSerializer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <A> void accumulateIn(RekordSerializer.Serializer<A> serializer) {
        for (Key<? super T, ?> key : keys()) {
            Key<? super T, Object> castKey = (Key<? super T, Object>) key;
            Object value = castKey.retrieveFrom(properties);
            castKey.accumulate(value, serializer);
        }
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
        return serialize(new StringSerializer());
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        @SafeVarargs
        public final Rekord<T> accepting(Key<? super T, ?>... keys) {
            return accepting(OrderedPSet.from(Arrays.asList(keys)));
        }

        public final Rekord<T> accepting(PSet<Key<? super T, ?>> keys) {
            return new Rekord<>(name, new Properties<>(keys));
        }
    }
}
