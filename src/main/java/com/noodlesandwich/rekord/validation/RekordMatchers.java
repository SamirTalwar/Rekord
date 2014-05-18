package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class RekordMatchers {
    private RekordMatchers() { }

    public static <T> RekordMatcher<T> aRekordOf(Class<T> type) {
        return aRekordNamed(type.getSimpleName());
    }

    public static <T> RekordMatcher<T> aRekordNamed(String name) {
        return new RekordMatcher<>(name);
    }

    public static <T> Matcher<FixedRekord<T>> that(final Check<T> check) {
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("an unspecified validation would pass");
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                mismatchDescription.appendText("it failed");
                return check.check(rekord);
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
    public static <T> Matcher<FixedRekord<T>> hasProperties(final Key<? super T, ?>... keys) {
        @SuppressWarnings("varargs")
        List<Key<? super T, ?>> keyList = Arrays.asList(keys);
        final PSet<Key<? super T, ?>> expectedKeys = OrderedPSet.from(keyList);
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

    public static <T, V> Matcher<FixedRekord<T>> hasProperty(final Key<T, V> key, final Matcher<V> expectedValue) {
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
