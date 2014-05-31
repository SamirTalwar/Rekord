package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class FunctionKey<T, V, W> extends DelegatingKey<T, V, W> {
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
    public Property<T, V> of(W value) {
        return new Property<>(underlyingKey, function.applyBackward(value));
    }

    @Override
    public W get(PropertyMap<? extends T> properties) {
        if (!test(properties)) {
            return null;
        }

        return function.applyForward(underlyingKey.get(properties));
    }

    @Override
    public boolean test(PropertyMap<? extends T> properties) {
        V value = underlyingKey.get(properties);
        return value != null && function.applyForward(value) != null;
    }

    @Override
    public <A, E extends Exception> void accumulate(W value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    public static final class UnwrappedFunctionKey {
        private final String name;

        private UnwrappedFunctionKey(String name) {
            this.name = name;
        }

        public <T, V> DysfunctionalFunctionKey<T, V> wrapping(Key<T, V> key) {
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
            return new FunctionKey<>(name, key, function);
        }
    }
}
