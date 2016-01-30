package com.noodlesandwich.rekord.keys;

import java.util.Collection;
import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class CollectionKey<T, V, C extends Collection<V>> extends OriginalKey<T, C> {
    private final Key<T, V> contents;

    private CollectionKey(String name, Key<T, V> contents) {
        super(name);
        this.contents = contents;
    }

    public static CollectionKeyBuilder named(String name) {
        return new CollectionKeyBuilder(name);
    }

    public static final class CollectionKeyBuilder {
        private final String name;

        public CollectionKeyBuilder(String name) {
            this.name = name;
        }

        public <T, V, C extends Collection<V>> CollectionKey<T, V, C> of(Key<T, V> contents) {
            return new CollectionKey<>(name, contents);
        }
    }

    @Override
    public <A, E extends Exception> void serialize(final C collection, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addCollection(name(), new Serializer.Accumulation<E>() {
            @Override
            public <A2> void accumulateIn(Serializer.Accumulator<A2, E> innerAccumulator) throws E {
                for (V value : collection) {
                    contents.serialize(value, innerAccumulator);
                }
            }
        });
    }

    @Override
    public <S, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, E> accumulator, Deserializer<S, E> deserializer) throws E {
        throw new UnsupportedOperationException();
    }
}
