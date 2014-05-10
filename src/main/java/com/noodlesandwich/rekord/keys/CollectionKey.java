package com.noodlesandwich.rekord.keys;

import java.util.Collection;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.Serialization;
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
    public <A> void accumulate(final Collection<V> collection, Serializer.Accumulator<A> accumulator) {
        accumulator.addCollection(name(), new Serializer.Accumulation<A>() {
            @Override
            public void accumulateIn(Serializer.Accumulator<A> collectionAccumulator) {
                Serialization.serialize(collection).with(contents).into(collectionAccumulator);
            }
        });
    }
}
