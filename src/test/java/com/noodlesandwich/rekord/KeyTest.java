package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Test;

import static com.noodlesandwich.rekord.Transformers.defaultsTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class KeyTest {
    @Test public void
    transforms_to_the_same_value_by_default() {
        assertThat(Thing.one.transform(1), is(1));
    }

    @Test public void
    transforms_null_to_null() {
        assertThat(Thing.two.transform(null), is(nullValue()));
    }

    @Test public void
    transforms_according_to_a_specified_transformer() {
        Key<Thing, String> key = Key.<Thing, String>named("key").that(upperCases());

        assertThat(key.transform("kablammo"), is("KABLAMMO"));
    }

    @Test public void
    delegates_to_internal_transformers() {
        Key<Thing, String> key = Key.<Thing, String>named("key").that(defaultsTo("nobody loves me")).then(upperCases());

        assertThat(key.transform(null), is("NOBODY LOVES ME"));
    }

    @Test public void
    allows_the_transformer_to_change_the_type() {
        Key<Thing, String> key = Key.<Thing, Integer>named("key").that(defaultsTo(88)).then(stringifies());

        assertThat(key.transform(97), is("97"));
        assertThat(key.transform(null), is("88"));
    }

    @Test public void
    stringifies_to_its_name() {
        assertThat(Thing.one, hasToString("one"));
    }

    private static interface Thing extends RekordType {
        Key<Thing, Integer> one = Key.named("one");
        Key<Thing, Integer> two = Key.named("two");
    }

    private static Transformer<String, String> upperCases() {
        return new Transformer<String, String>() {
            @Override public String transformInput(String value) {
                throw new UnsupportedOperationException();
            }

            @Override public String transformOutput(String value) {
                return value.toUpperCase();
            }
        };
    }

    private static Transformer<Integer, String> stringifies() {
        return new Transformer<Integer, String>() {
            @Override public Integer transformInput(String value) {
                return Integer.parseInt(value);
            }

            @Override public String transformOutput(Integer value) {
                return value.toString();
            }
        };
    }
}
