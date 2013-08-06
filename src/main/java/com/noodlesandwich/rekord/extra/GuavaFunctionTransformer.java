package com.noodlesandwich.rekord.extra;

import com.google.common.base.Function;
import com.noodlesandwich.rekord.transformers.Transformer;

public class GuavaFunctionTransformer<F, T> implements Transformer<F, T> {
    private final Function<? super T, ? extends F> inputFunction;
    private final Function<? super F, ? extends T> outputFunction;

    public GuavaFunctionTransformer(Function<? super T, ? extends F> inputFunction, Function<? super F, ? extends T> outputFunction) {
        this.inputFunction = inputFunction;
        this.outputFunction = outputFunction;
    }

    @Override
    public F transformInput(T value) {
        return inputFunction.apply(value);
    }

    @Override
    public T transformOutput(F value) {
        return outputFunction.apply(value);
    }
}
