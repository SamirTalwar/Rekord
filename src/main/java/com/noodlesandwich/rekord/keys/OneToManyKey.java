package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class OneToManyKey<T, V> extends DelegatingKey<T, V> {
    private final Keys<T> keys;
    private final InvertibleFunction<PropertyMap<T>, V> function;

    public OneToManyKey(String name, Keys<T> keys, InvertibleFunction<PropertyMap<T>, V> function) {
        super(name, keys);
        this.keys = keys;
        this.function = function;
    }

    public static UnmappedOneToManyKey named(String name) {
        return new UnmappedOneToManyKey(name);
    }

    @Override
    public <R extends T> boolean test(PropertyMap<R> properties) {
        for (Key<? super T, ?> key : keys) {
            if (!key.test(properties)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends T> V get(PropertyMap<R> properties) {
        return function.applyForward((PropertyMap<T>) properties);
    }

    @Override
    public <R extends T> PropertyMap<R> set(V value, PropertyMap<R> properties) {
        PropertyMap<R> newProperties = properties;
        for (Property<? super T, ?> property : function.applyBackward(value)) {
            newProperties = newProperties.set(property);
        }
        return newProperties;
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    public static final class UnmappedOneToManyKey {
        private final String name;

        private UnmappedOneToManyKey(String name) {
            this.name = name;
        }

        // CHECKSTYLE:OFF
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final <T> DysfunctionalOneToManyKey<T> over(Keys<T>... keys) {
            return over(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public <T> DysfunctionalOneToManyKey<T> over(Keys<T> keys) {
            return new DysfunctionalOneToManyKey<>(name, keys);
        }
    }

    public static final class DysfunctionalOneToManyKey<T> {
        private final String name;
        private final Keys<T> keys;

        public DysfunctionalOneToManyKey(String name, Keys<T> keys) {
            this.name = name;
            this.keys = keys;
        }

        public <V> OneToManyKey<T, V> with(InvertibleFunction<PropertyMap<T>, V> function) {
            return new OneToManyKey<>(name, keys, function);
        }
    }
}
