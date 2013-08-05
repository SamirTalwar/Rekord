package com.noodlesandwich.rekord.extra;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;

public class GuavaTransformers {
    public static <F, T> Transformer<F, T> fromFunction(Function<F, T> function) {
        return new GuavaFunctionTransformer<>(function);
    }

    public static <T> Transformer<T, Optional<T>> asOptional() {
        return new GuavaOptionalTransformer<>();
    }
}
