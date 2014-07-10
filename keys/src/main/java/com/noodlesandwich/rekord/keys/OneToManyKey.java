package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;

public final class OneToManyKey<T, V> extends DelegatingKey<T, V> {
    private final Keys<T> keys;
    private final InvertibleFunction<Properties<T>, V> function;

    public OneToManyKey(String name, Keys<T> keys, InvertibleFunction<Properties<T>, V> function) {
        super(name, keys);
        this.keys = keys;
        this.function = function;
    }

    public static UnmappedOneToManyKey named(String name) {
        return new UnmappedOneToManyKey(name);
    }

    @Override
    public <R extends T> boolean test(Properties<R> properties) {
        for (Key<? super T, ?> key : keys) {
            if (!key.test(properties)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends T> V get(Properties<R> properties) {
        return function.applyForward((Properties<T>) properties);
    }

    @Override
    public <R extends T> Properties<R> set(V value, Properties<R> properties) {
        Properties<R> newProperties = properties;
        for (Property<? super T, ?> property : function.applyBackward(value)) {
            newProperties = newProperties.set(property);
        }
        return newProperties;
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

        public <V> OneToManyKey<T, V> with(InvertibleFunction<Properties<T>, V> function) {
            return new OneToManyKey<>(name, keys, function);
        }
    }
}
