package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.transformers.Transformer;
import com.noodlesandwich.rekord.transformers.Transformers;

public final class TransformingKey<T, U, V> extends AbstractKey<T, V> {
    private final Key<T, U> original;
    private final Transformer<U, V> transformer;

    public TransformingKey(Key<T, U> original, Transformer<U, V> transformer) {
        this.original = original;
        this.transformer = transformer;
    }

    @Override
    public String name() {
        return original.name();
    }

    @Override
    public <P extends T> Properties<P> storeTo(Properties<P> properties, V value) {
        return properties.with(new Property<P, U>(original, transformer.transformInput(value)));
    }

    @Override
    public <P extends T> V retrieveFrom(Properties<P> properties) {
        return transformer.transformOutput(properties.get(original));
    }

    @Override
    public <NewV> TransformingKey<T, U, NewV> that(Transformer<V, NewV> next) {
        return new TransformingKey<>(original, Transformers.compose(next, transformer));
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        original.accumulate(transformer.transformInput(value), accumulator);
    }

    @Override
    public Key<T, U> original() {
        return original;
    }
}
