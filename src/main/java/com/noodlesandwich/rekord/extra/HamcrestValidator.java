package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.transformers.Transformer;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public final class HamcrestValidator<T> implements Transformer<T, T> {
    private final Matcher<T> matcher;

    public HamcrestValidator(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public T transformInput(T value) {
        if (!matcher.matches(value)) {
            StringDescription mismatchDescription = new StringDescription();
            matcher.describeMismatch(value, mismatchDescription);
            throw new ValidationException(mismatchDescription.toString());
        }

        return value;
    }

    @Override
    public T transformOutput(T value) {
        return value;
    }
}
