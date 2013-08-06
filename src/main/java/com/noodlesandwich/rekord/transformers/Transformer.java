package com.noodlesandwich.rekord.transformers;

public interface Transformer<From, To> {
    From transformInput(To value);

    To transformOutput(From value);
}
