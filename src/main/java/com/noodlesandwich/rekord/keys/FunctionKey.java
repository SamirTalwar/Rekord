package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;

public final class FunctionKey<T, V, W> extends DelegatingKey<T, W> {
    private final Key<T, V> underlyingKey;
    private final InvertibleFunction<V, W> function;

    private FunctionKey(String name, Key<T, V> underlyingKey, InvertibleFunction<V, W> function) {
        super(name, underlyingKey);
        this.underlyingKey = underlyingKey;
        this.function = function;
    }

    public static UnwrappedFunctionKey named(String name) {
        return new UnwrappedFunctionKey(name);
    }

    public static <T, V> DysfunctionalFunctionKey<T, V> wrapping(Key<T, V> key) {
        return named(key.name()).wrapping(key);
    }

    @Override
    public <R extends T> boolean test(Properties<R> properties) {
        V value = underlyingKey.get(properties);
        return value != null && function.applyForward(value) != null;
    }

    @Override
    public <R extends T> W get(Properties<R> properties) {
        if (!test(properties)) {
            return null;
        }

        return function.applyForward(underlyingKey.get(properties));
    }

    @Override
    public <R extends T> Properties<R> set(W value, Properties<R> properties) {
        return properties.set(new Property<>(underlyingKey, function.applyBackward(value)));
    }

    public static final class UnwrappedFunctionKey {
        private final String name;

        private UnwrappedFunctionKey(String name) {
            this.name = name;
        }

        public <T, V> DysfunctionalFunctionKey<T, V> wrapping(Key<T, V> key) {
            if (key == null) {
                throw new NullPointerException("The underlying key of a FunctionKey must not be null.");
            }
            return new DysfunctionalFunctionKey<>(name, key);
        }
    }

    public static final class DysfunctionalFunctionKey<T, V> {
        private final String name;
        private final Key<T, V> key;

        private DysfunctionalFunctionKey(String name, Key<T, V> key) {
            this.name = name;
            this.key = key;
        }

        public <W> FunctionKey<T, V, W> with(InvertibleFunction<V, W> function) {
            if (function == null) {
                throw new NullPointerException("The function of a FunctionKey must not be null.");
            }
            return new FunctionKey<>(name, key, function);
        }
    }
}
