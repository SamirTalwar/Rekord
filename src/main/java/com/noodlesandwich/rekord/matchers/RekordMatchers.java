package com.noodlesandwich.rekord.matchers;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordType;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class RekordMatchers {
    public static <T extends RekordType> RekordMatcher<T> aRekordOf(Class<T> type) {
        return new RekordMatcher<>(Rekord.of(type));
    }

    public static <T extends RekordType> RekordMatcher<T> aRekordNamed(String name) {
        return new RekordMatcher<>(Rekord.<T>create(name));
    }

    public static final class RekordMatcher<T extends RekordType> extends TypeSafeDiagnosingMatcher<Rekord<T>> {
        private Rekord<T> expectedRekord;

        public RekordMatcher(Rekord<T> emptyRekord) {
            expectedRekord = emptyRekord;
        }

        public <V> RekordMatcher<T> with(Key<? super T, V> key, V value) {
            expectedRekord = expectedRekord.with(key, value);
            return this;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a rekord that looks like ").appendValue(expectedRekord);
        }

        @Override
        protected boolean matchesSafely(Rekord<T> actualRekord, Description mismatchDescription) {
            mismatchDescription.appendText("a rekord that looks like ").appendValue(actualRekord);
            return expectedRekord.equals(actualRekord);
        }
    }
}
