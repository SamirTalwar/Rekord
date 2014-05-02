package com.noodlesandwich.rekord.transformers;

public final class IdentityTransformer<T> implements Transformer<T, T> {
    @Override
    public T transformInput(T value) {
        return value;
    }

    @Override
    public T transformOutput(T value) {
        return value;
    }
}
