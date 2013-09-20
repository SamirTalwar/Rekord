package com.noodlesandwich.rekord.extra;

import org.hamcrest.Matcher;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class Validation {
    public static <T> Transformer<T, T> validatesItsInput(Matcher<T> matcher) {
        return new HamcrestValidator<>(matcher);
    }
}
