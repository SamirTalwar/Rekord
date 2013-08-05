package com.noodlesandwich.rekord.extra;

import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;

public class GuavaOptionalTransformer<T> implements Transformer<T, Optional<T>> {
    @Override
    public Optional<T> transform(T value) {
        return Optional.fromNullable(value);
    }
}
