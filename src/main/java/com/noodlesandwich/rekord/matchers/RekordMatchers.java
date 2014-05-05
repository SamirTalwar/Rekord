package com.noodlesandwich.rekord.matchers;

import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import static org.hamcrest.Matchers.equalTo;

public final class RekordMatchers {
    public static <T> RekordMatcher<T> aRekordOf(Class<T> type) {
        return aRekordNamed(type.getSimpleName());
    }

    public static <T> RekordMatcher<T> aRekordNamed(String name) {
        return new RekordMatcher<>(name);
    }

    public static <T> Matcher<FixedRekord<T>> hasKey(Key<T, ?> key) {
        return new RekordKeyMatcher<>(key);
    }

    public static final class RekordMatcher<T> extends TypeSafeDiagnosingMatcher<FixedRekord<T>> {
        private final String name;
        private PMap<Key<? super T, ?>, Matcher<?>> expectedProperties = HashTreePMap.empty();

        private RekordMatcher(String name) {
            this.name = name;
        }

        public <V> RekordMatcher<T> with(Key<? super T, V> key, V value) {
            with(key, equalTo(value));
            return this;
        }

        public <V> RekordMatcher<T> with(Key<? super T, V> key, Matcher<V> valueMatcher) {
            expectedProperties = expectedProperties.plus(key, valueMatcher);
            return this;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a rekord that looks like ").appendText(name).appendValue(expectedProperties);
        }

        @Override
        protected boolean matchesSafely(FixedRekord<T> actualRekord, Description mismatchDescription) {
            mismatchDescription.appendText("a rekord that looks like ").appendValue(actualRekord);

            if (!expectedProperties.keySet().equals(actualRekord.keys())) {
                return false;
            }

            for (Map.Entry<Key<? super T, ?>, Matcher<?>> property : expectedProperties.entrySet()) {
                Key<? super T, ?> expectedKey = property.getKey();
                Matcher<?> expectedValue = property.getValue();
                if (!expectedValue.matches(actualRekord.get(expectedKey))) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final class RekordKeyMatcher<T> extends TypeSafeDiagnosingMatcher<FixedRekord<T>> {
        private final Key<T, ?> key;

        public RekordKeyMatcher(Key<T, ?> key) {
            this.key = key;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has the key ").appendValue(key);
        }

        @Override
        protected boolean matchesSafely(FixedRekord<T> rekord, Description mismatchDescription) {
            mismatchDescription.appendText("the rekord ").appendValue(rekord).appendText(" did not have the key ").appendValue(key);
            return rekord.containsKey(key);
        }
    }
}
