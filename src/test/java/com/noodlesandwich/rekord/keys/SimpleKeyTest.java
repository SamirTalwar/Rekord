package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class SimpleKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties<Thing> properties = new Properties<>(Thing.keys)
                .with(Thing.one.of(1))
                .with(Thing.two.of(2));

        assertThat(Thing.one.get(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties<Thing> properties = new Properties<>(Thing.keys)
                .with(Thing.one.of(5));

        assertThat(Thing.two.get(properties), is(nullValue()));
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

        KeySet<Thing> keys = Keys.from(one, two);
    }
}
