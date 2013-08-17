package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.transformers.Transformer;
import org.hamcrest.Matcher;

public final class Validation {
    public static <T> Transformer<T, T> validatesItsInput(Matcher<T> matcher) {
        return new HamcrestValidator<>(matcher);
    }
}
