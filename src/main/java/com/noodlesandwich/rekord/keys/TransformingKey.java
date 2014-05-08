package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.transformers.Transformer;
import com.noodlesandwich.rekord.transformers.Transformers;

public final class TransformingKey<T, U, V> extends Key<T, V> {
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
    public <NewV> TransformingKey<T, U, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(original, Transformers.compose(transformer, this.transformer));
    }

    @Override
    public <P extends T> Properties<P> storeTo(Properties<P> properties, V value) {
        return properties.with(new Property(this, transformer.transformInput(value)));
    }

    @Override
    public <P extends T> V retrieveFrom(Properties<P> properties) {
        return transformer.transformOutput(properties.get(original));
    }

    @Override
    public <A> void accumulate(V value, RekordSerializer.Serializer<A> serializer) {
        original.accumulate(transformer.transformInput(value), serializer);
    }

    @Override
    public Key<T, U> original() {
        return original;
    }
}
