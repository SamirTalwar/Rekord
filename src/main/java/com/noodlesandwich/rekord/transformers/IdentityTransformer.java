package com.noodlesandwich.rekord.transformers;

public class IdentityTransformer<T> implements Transformer<T, T> {
    @Override public T transform(T value) {
        return value;
    }
}
