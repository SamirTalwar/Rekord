package com.noodlesandwich.rekord.transformers;

public final class DefaultingTransformer<T> implements Transformer<T, T> {
    private final T defaultValue;

    public DefaultingTransformer(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public T transformInput(T value) {
        return value;
    }

    @Override
    public T transformOutput(T value) {
        return value != null ? value : defaultValue;
    }
}
