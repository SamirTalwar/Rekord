package com.noodlesandwich.rekord.extra;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class HamcrestValidator<T> implements Transformer<T, T> {
    private final Matcher<T> matcher;

    public HamcrestValidator(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Override
    public T transformInput(T value) {
        validate(value);
        return value;
    }

    @Override
    public T transformOutput(T value) {
        validate(value);
        return value;
    }

    private void validate(T value) {
        if (!matcher.matches(value)) {
            StringDescription mismatchDescription = new StringDescription();
            matcher.describeMismatch(value, mismatchDescription);
            throw new ValidationException(mismatchDescription.toString());
        }
    }
}
