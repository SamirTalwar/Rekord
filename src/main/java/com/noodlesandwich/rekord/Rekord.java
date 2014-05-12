package com.noodlesandwich.rekord;

import java.util.Arrays;
import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.properties.Properties;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Rekord<T> extends AbstractFixedRekord<T> implements RekordBuilder<T, Rekord<T>> {
    private final Properties<T> properties;

    public Rekord(String name, Properties<T> properties) {
        super(name, properties);
        this.properties = properties;
    }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        return new Rekord<>(name(), key.storeTo(properties, value));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name(), properties.without(key));
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rekord && super.equals(other);
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
