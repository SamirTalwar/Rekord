package com.noodlesandwich.rekord.validation;

import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.pcollections.PMap;

import static org.hamcrest.CoreMatchers.equalTo;

public final class RekordMatcher<T> extends TypeSafeDiagnosingMatcher<FixedRekord<T>> {
    private final String name;
    private final PMap<Key<? super T, ?>, Matcher<?>> expectedProperties;

    public RekordMatcher(String name, PMap<Key<? super T, ?>, Matcher<?>> expectedProperties) {
        this.name = name;
        this.expectedProperties = expectedProperties;
    }

    public <V> RekordMatcher<T> with(Key<? super T, V> key, V value) {
        return with(key, equalTo(value));
    }

    public <V> RekordMatcher<T> with(Key<? super T, V> key, Matcher<V> valueMatcher) {
        return new RekordMatcher<>(name, expectedProperties.plus(key, valueMatcher));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a rekord that looks like ").appendText(name).appendValue(expectedProperties);
    }

    @Override
    protected boolean matchesSafely(FixedRekord<T> actualRekord, Description mismatchDescription) {
        mismatchDescription.appendText("a rekord that looks like ").appendValue(actualRekord);

        if (!expectedProperties.keySet().equals(actualRekord.keys().toSet())) {
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
