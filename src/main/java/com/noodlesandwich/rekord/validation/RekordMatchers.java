package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

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

    public static <T, V> Matcher<FixedRekord<T>> hasKey(final Key<T, V> key) {
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("has a property with the key ").appendValue(key);
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                mismatchDescription.appendText("did not have a property with the key ").appendValue(key);
                return rekord.has(key);
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
                Keys<T> expectedKeys = rekord.acceptedKeys();
                Keys<T> actualKeys = rekord.keys();
                Set<Key<? super T, ?>> missingKeys = difference(expectedKeys, actualKeys);
                mismatchDescription.appendText("was missing the keys ").appendValue(missingKeys);
                return missingKeys.isEmpty();
            }
        };
    }

    @SafeVarargs
    public static <T> Matcher<FixedRekord<T>> hasProperties(final Key<? super T, ?>... keys) {
        @SuppressWarnings("varargs")
        final Collection<Key<? super T, ?>> expectedKeys = Arrays.asList(keys);
        return new TypeSafeDiagnosingMatcher<FixedRekord<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("the rekord has properties with the keys ").appendValue(expectedKeys);
            }

            @Override
            protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
                Keys<T> actualKeys = rekord.keys();
                Set<Key<? super T, ?>> missingKeys = difference(expectedKeys, actualKeys);
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

    private static <T> Set<Key<? super T, ?>> difference(Iterable<Key<? super T, ?>> a, Keys<T> b) {
        Set<Key<? super T, ?>> missingKeys = new HashSet<>();
        for (Key<? super T, ?> key : a) {
            if (!b.contains(key)) {
                missingKeys.add(key);
            }
        }
        return missingKeys;
    }
}
