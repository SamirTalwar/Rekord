package com.noodlesandwich.rekord.extra;

import com.google.common.base.Function;
import com.noodlesandwich.rekord.transformers.Transformer;

public class GuavaFunctionTransformer<F, T> implements Transformer<F, T> {
    private final Function<F, T> function;

    public GuavaFunctionTransformer(Function<F, T> function) {
        this.function = function;
    }

    @Override
    public T transform(F value) {
        return function.apply(value);
    }
}
