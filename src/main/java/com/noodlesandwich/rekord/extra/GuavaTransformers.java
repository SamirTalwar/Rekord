package com.noodlesandwich.rekord.extra;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class GuavaTransformers {
    public static <F, T> Transformer<F, T> fromFunctions(Function<? super T, ? extends F> inputFunction, Function<? super F, ? extends T> outputFunction) {
        return new GuavaFunctionTransformer<>(inputFunction, outputFunction);
    }

    public static <T> Transformer<T, Optional<T>> asOptional() {
        return new GuavaOptionalTransformer<>();
    }
}
