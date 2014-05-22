package com.noodlesandwich.rekord;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.LimitedPropertyMap;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Property;

public final class Rekord<T> extends AbstractFixedRekord<T> implements RekordBuilder<T, Rekord<T>> {
    private final LimitedPropertyMap<T> properties;

    private Rekord(String name, LimitedPropertyMap<T> properties) {
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
    public <V> Rekord<T> with(Property<? super T, V> property) {
        return new Rekord<>(name(), properties.with(property));
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        return with(key.of(value));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return with(key.of(value));
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return new Rekord<>(name(), properties.without(key));
    }

    public Rekord<T> merge(FixedRekord<T> other) {
        Rekord<T> result = this;
        for (Property<? super T, ?> property : other.properties()) {
            result = result.with(property);
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rekord && abstractEquals(other);
    }

    @Override
    public int hashCode() {
        return abstractHashCode();
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        private UnkeyedRekord(String name) {
            this.name = name;
        }

        // CHECKSTYLE:OFF
        @SafeVarargs
        public final Rekord<T> accepting(Keys<? super T>... keys) {
            @SuppressWarnings("varargs")
            List<Keys<? super T>> keyList = Arrays.asList(keys);
            return accepting(KeySet.from(keyList));
        }
        // CHECKSTYLE:ON

        public Rekord<T> accepting(Keys<T> keys) {
            return new Rekord<>(name, new LimitedPropertyMap<>(keys));
        }
    }
}
