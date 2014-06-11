package com.noodlesandwich.rekord.keys;

import java.util.Iterator;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class ComposedKey<T, V, W> extends AbstractKey<T, W> {
    private final BuildableKey<T, Rekord<V>, Rekord<V>> before;
    private final Key<V, W> after;

    private ComposedKey(String name, BuildableKey<T, Rekord<V>, Rekord<V>> before, Key<V, W> after) {
        super(name);
        this.before = before;
        this.after = after;
    }

    public static UnsourcedComposedKey named(String name) {
        return new UnsourcedComposedKey(name);
    }

    @Override
    public <R extends T> boolean test(Properties<R> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends T> W get(Properties<R> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <R extends T> Properties<R> set(W value, Properties<R> properties) {
        Rekord<V> inner = before.builder().with(after, value);
        return before.set(inner, properties);
    }

    @Override
    public <A, E extends Exception> void accumulate(W value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Key<? super T, ?>> iterator() {
        throw new UnsupportedOperationException();
    }

    public static final class UnsourcedComposedKey {
        private final String name;

        private UnsourcedComposedKey(String name) {
            this.name = name;
        }

        public <T, V> UndirectedComposedKey<T, V> composing(BuildableKey<T, Rekord<V>, Rekord<V>> before) {
            return new UndirectedComposedKey<>(name, before);
        }
    }

    public static final class UndirectedComposedKey<T, V> {
        private final String name;
        private final BuildableKey<T, Rekord<V>, Rekord<V>> before;

        private UndirectedComposedKey(String name, BuildableKey<T, Rekord<V>, Rekord<V>> before) {
            this.name = name;
            this.before = before;
        }

        public <W> Key<T, W> with(Key<V, W> after) {
            return new ComposedKey<>(name, before, after);
        }
    }
}
