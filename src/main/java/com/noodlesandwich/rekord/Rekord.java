package com.noodlesandwich.rekord;

import java.util.Arrays;
import com.noodlesandwich.rekord.implementation.FixedRekordDelegate;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Rekord<T> implements RekordBuilder<T, Rekord<T>>, FixedRekord<T> {
    private final Properties<T> properties;
    private final FixedRekord<T> delegate;

    public Rekord(String name, Properties<T> properties) {
        this.properties = properties;
        this.delegate = new FixedRekordDelegate<>(name, properties);
    }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public <V> V get(Key<? super T, V> key) {
        return delegate.get(key);
    }

    @Override
    public boolean has(Key<T, ?> key) {
        return delegate.has(key);
    }

    @Override
    public PSet<Key<? super T, ?>> keys() {
        return delegate.keys();
    }

    @Override
    public PSet<Key<? super T, ?>> acceptedKeys() {
        return delegate.acceptedKeys();
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        return new Rekord<>(delegate.name(), key.storeTo(properties, value));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(delegate.name(), properties.without(key));
    }

    @Override
    public <R> R serialize(Serializer<R> serializer) {
        return delegate.serialize(serializer);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Rekord)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Rekord<T> that = (Rekord<T>) other;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
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
