package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.validation.InvalidRekordException;
import com.noodlesandwich.rekord.validation.Validator;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public final class HamcrestValidator<T, V> implements Validator<T> {
    private final Key<T, V> key;
    private final Matcher<V> expectedValue;

    public static <T, V> Validator<T> theProperty(Key<T, V> key, Matcher<V> expectedValue) {
        return new HamcrestValidator<>(key, expectedValue);
    }

    private HamcrestValidator(Key<T, V> key, Matcher<V> expectedValue) {
        this.key = key;
        this.expectedValue = expectedValue;
    }

    @Override
    public void test(FixedRekord<T> rekord) throws InvalidRekordException {
        V actualValue = rekord.get(key);
        if (!expectedValue.matches(actualValue)) {
            StringDescription mismatchDescription = new StringDescription();
            expectedValue.describeMismatch(actualValue, mismatchDescription);
            throw new InvalidRekordException(mismatchDescription.toString());
        }
    }
}
