package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class FunctionKey<T, V, W> extends AbstractKey<T, W> {
    private final Key<T, V> key;
    private final InvertibleFunction<V, W> function;

    private FunctionKey(String name, Key<T, V> key, InvertibleFunction<V, W> function) {
        super(name);
        this.key = key;
        this.function = function;
    }

    public static UnwrappedKeyLens named(String name) {
        return new UnwrappedKeyLens(name);
    }

    @Override
    public Property<T, V> of(W value) {
        return new Property<>(key, function.applyBackward(value));
    }

    @Override
    public W get(PropertyMap<? extends T> properties) {
        if (!key.test(properties)) {
            return null;
        }

        return function.applyForward(key.get(properties));
    }

    @Override
    public boolean test(PropertyMap<? extends T> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A, E extends Exception> void accumulate(W value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    public static final class UnwrappedKeyLens {
        private final String name;

        private UnwrappedKeyLens(String name) {
            this.name = name;
        }

        public <T, V> DysfunctionalKeyLens<T, V> wrapping(Key<T, V> key) {
            return new DysfunctionalKeyLens<>(name, key);
        }
    }

    public static final class DysfunctionalKeyLens<T, V> {
        private final String name;
        private final Key<T, V> key;

        private DysfunctionalKeyLens(String name, Key<T, V> key) {
            this.name = name;
            this.key = key;
        }

        public <W> FunctionKey<T, V, W> with(InvertibleFunction<V, W> function) {
            return new FunctionKey<>(name, key, function);
        }
    }
}
