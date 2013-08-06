package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class NamedKey<T, V> extends Key<T, V> {
    private final String name;

    public NamedKey(String name) {
        this.name = name;
    }

    @Override
    public <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(this, transformer);
    }

    @Override
    public Properties storeTo(Properties properties, V value) {
        return properties.with(new Property<>(this, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Properties properties) {
        return (V) properties.get(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
