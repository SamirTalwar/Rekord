package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.PersistentPropertyMap;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import com.noodlesandwich.rekord.properties.PropertyMap;

public final class Rekord<T> extends AbstractFixedRekord<T> implements RekordBuilder<T, Rekord<T>> {
    private final Keys<T> acceptedKeys;
    private final PropertyMap<T> properties;

    private Rekord(String name, Keys<T> acceptedKeys, PropertyMap<T> properties) {
        super(name, acceptedKeys, properties);
        this.acceptedKeys = acceptedKeys;
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
        return set(properties.set(property));
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        return set(key.set(value, properties));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return set(key.set(value, properties));
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return set(properties.remove(key));
    }

    private Rekord<T> set(PropertyMap<T> newProperties) {
        PropertyKeys.checkAcceptabilityOf(newProperties, acceptedKeys);
        return new Rekord<>(name(), acceptedKeys, newProperties);
    }

    @Override
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
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final Rekord<T> accepting(Keys<? super T>... keys) {
            return accepting(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public Rekord<T> accepting(Keys<T> keys) {
            return new Rekord<>(name, keys, new PersistentPropertyMap<T>());
        }
    }
}
