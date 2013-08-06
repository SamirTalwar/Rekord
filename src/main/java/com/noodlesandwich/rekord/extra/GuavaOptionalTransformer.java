package com.noodlesandwich.rekord.extra;

import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;

public class GuavaOptionalTransformer<T> implements Transformer<T, Optional<T>> {
    @Override
    public T transformInput(Optional<T> value) {
        return value.orNull();
    }

    @Override
    public Optional<T> transformOutput(T value) {
        return Optional.fromNullable(value);
    }
}
