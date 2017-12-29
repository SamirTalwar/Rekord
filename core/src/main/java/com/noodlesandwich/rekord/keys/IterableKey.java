package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class IterableKey<T, V, C extends Iterable<V>> extends OriginalKey<T, C> {
    private final Key<T, V> contents;

    private IterableKey(String name, Key<T, V> contents) {
        super(name);
        this.contents = contents;
    }

    public static IterableKeyBuilder named(String name) {
        return new IterableKeyBuilder(name);
    }

    public static final class IterableKeyBuilder {
        private final String name;

        public IterableKeyBuilder(String name) {
            this.name = name;
        }

        public <T, V, C extends Iterable<V>> IterableKey<T, V, C> of(Key<T, V> contents) {
            return new IterableKey<>(name, contents);
        }
    }

    @Override
    public <A, E extends Exception> void serialize(final C iterable, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addIterable(name(), new Serializer.Accumulation<E>() {
            @Override
            public <A2> void accumulateIn(Serializer.Accumulator<A2, E> iterableAccumulator) throws E {
                for (V value : iterable) {
                    contents.serialize(value, iterableAccumulator);
                }
            }
        });
    }

    @Override
    public <S, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, E> accumulator, Deserializer<S, E> deserializer) throws E {
        throw new UnsupportedOperationException();
    }
}
