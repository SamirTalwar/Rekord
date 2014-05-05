package com.noodlesandwich.rekord.keys;

import java.util.Collection;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class CollectionKey<T, V> extends OriginalKey<T, Collection<V>> {
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

        public <T, V> Key<T, Collection<V>> of(Key<T, V> contents) {
            return new CollectionKey<>(name, contents);
        }
    }

    @Override
    public <A> void accumulate(Collection<V> collection, Serializer.Accumulator<A> accumulator) {
        Serializer.Accumulator<A> collectionAccumulator = accumulator.collection(name());
        for (V value : collection) {
            contents.accumulate(value, collectionAccumulator);
        }
        accumulator.accumulate(name(), collectionAccumulator);
    }
}
