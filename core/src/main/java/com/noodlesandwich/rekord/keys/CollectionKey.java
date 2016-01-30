package com.noodlesandwich.rekord.keys;

import java.util.Collection;
import com.noodlesandwich.rekord.buildables.Buildable;
import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class CollectionKey<T, V, C extends Collection<V>> extends OriginalKey<T, C> implements BuildableKey<T, C> {
    private final Key<T, V> contents;
    private final Buildable<? extends C> buildable;

    private CollectionKey(String name, Key<T, V> contents, Buildable<? extends C> buildable) {
        super(name);
        this.contents = contents;
        this.buildable = buildable;
    }

    public static UncontainableCollectionKey named(String name) {
        return new UncontainableCollectionKey(name);
    }

    @Override
    public C builder() {
        return buildable.builder();
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

    public static final class UncontainableCollectionKey {
        private final String name;

        public UncontainableCollectionKey(String name) {
            this.name = name;
        }

        public <T, V> UnbuildableCollectionKey<T, V> of(Key<T, V> contents) {
            return new UnbuildableCollectionKey<>(name, contents);
        }
    }

    public static final class UnbuildableCollectionKey<T, V> {
        private final String name;
        private final Key<T, V> contents;

        public UnbuildableCollectionKey(String name, Key<T, V> contents) {
            this.name = name;
            this.contents = contents;
        }

        public <C extends Collection<V>> CollectionKey<T, V, C> builtFrom(Buildable<? extends C> buildable) {
            return new CollectionKey<>(name, contents, buildable);
        }
    }
}
