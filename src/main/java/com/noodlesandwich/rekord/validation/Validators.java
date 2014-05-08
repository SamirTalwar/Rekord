package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Validators {
    private Validators() { }

    public static <T> Matcher<FixedRekord<T>> that(final BooleanValidator<T> validator) {
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("an unspecified validation would pass");
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                mismatchDescription.appendText("it failed");
                return validator.test(rekord);
            }
        };
    }

    public static <T> Matcher<FixedRekord<T>> allProperties() {
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("all properties are set");
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                PSet<Key<? super T, ?>> expectedKeys = rekord.acceptedKeys();
                PSet<Key<? super T, ?>> actualKeys = rekord.keys();
                PSet<Key<? super T, ?>> missingKeys = expectedKeys.minusAll(actualKeys);
                mismatchDescription.appendText("was missing the keys ").appendValue(missingKeys);
                return missingKeys.isEmpty();
            }
        };
    }

    @SafeVarargs
    public static <T> Matcher<FixedRekord<T>> theProperties(final Key<? super T, ?>... keys) {
        final PSet<Key<? super T, ?>> expectedKeys = OrderedPSet.from(Arrays.asList(keys));
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("the rekord has properties with the keys ").appendValue(expectedKeys);
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                PSet<Key<? super T, ?>> actualKeys = rekord.keys();
                PSet<Key<? super T, ?>> missingKeys = expectedKeys.minusAll(actualKeys);
                mismatchDescription.appendText("was missing the keys ").appendValue(missingKeys);
                return missingKeys.isEmpty();
            }
        };
    }

    public static <T, V> Matcher<FixedRekord<T>> theProperty(final Key<T, V> key, final Matcher<V> expectedValue) {
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description
                        .appendText("the rekord has the property ").appendValue(key)
                        .appendText(" with the value ").appendDescriptionOf(expectedValue);
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                V actualValue = rekord.get(key);
                mismatchDescription.appendText("the value was ").appendValue(actualValue);
                return expectedValue.matches(actualValue);
            }
        };
    }
}
