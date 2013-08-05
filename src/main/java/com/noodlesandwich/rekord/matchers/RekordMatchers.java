package com.noodlesandwich.rekord.matchers;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordType;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public final class RekordMatchers {
    public static <T extends RekordType> RekordMatcher<T> aRekordOf(Class<T> type) {
        return new RekordMatcher<>(type);
    }

    public static final class RekordMatcher<T extends RekordType> extends TypeSafeDiagnosingMatcher<Rekord<T>> {
        private final Rekord.RekordBuilder<T> rekordBuilder;

        public RekordMatcher(Class<T> type) {
            rekordBuilder = Rekord.of(type);
        }

        public <V> RekordMatcher<T> with(Key<? super T, V> key, V value) {
            rekordBuilder.with(key, value);
            return this;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a rekord that looks like ").appendValue(rekordBuilder.build());
        }

        @Override
        protected boolean matchesSafely(Rekord<T> rekord, Description mismatchDescription) {
            mismatchDescription.appendText("a rekord that looks like ").appendValue(rekord);
            return rekordBuilder.build().equals(rekord);
        }
    }
}
