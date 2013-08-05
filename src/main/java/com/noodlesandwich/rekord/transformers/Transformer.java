package com.noodlesandwich.rekord.transformers;

public interface Transformer<From, To> {
    To transform(From value);
}
