package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class SimplePropertyTest {
    @Test public void
    retrieves_a_value() {
        Rekord<Thing> rekord = Thing.rekord
                .with(Thing.one.of(1))
                .with(Thing.two.of(2));

        assertThat(rekord.get(Thing.one), is(1));
    }

    @Test public void
    returns_null_if_the_rekord_does_not_contain_the_key() {
        Rekord<Thing> rekord = Thing.rekord
                .with(Thing.one.of(5));

        assertThat(rekord.get(Thing.two), is(nullValue()));
    }

    @Test public void
    knows_its_name() {
        assertThat(Thing.one.name(), is("one"));
    }

    @Test public void
    stringifies_to_its_name() {
        assertThat(Thing.one, hasToString("one"));
    }

    private static interface Thing {
        Key<Thing, Integer> one = SimpleKey.named("one");
        Key<Thing, Integer> two = SimpleKey.named("two");

        Rekord<Thing> rekord = Rekord.of(Thing.class).accepting(one, two);
    }
}
