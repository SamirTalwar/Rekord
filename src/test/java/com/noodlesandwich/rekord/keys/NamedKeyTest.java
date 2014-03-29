package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class NamedKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties properties = new Properties(Thing.keys);
        properties = Thing.one.storeTo(properties, 1);
        properties = Thing.two.storeTo(properties, 2);

        MatcherAssert.assertThat(Thing.one.retrieveFrom(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties properties = new Properties(Thing.keys);
        properties = Thing.one.storeTo(properties, 5);

        MatcherAssert.assertThat(Thing.two.retrieveFrom(properties), is(nullValue()));
    }

    @Test public void
    stringifies_to_its_name() {
        MatcherAssert.assertThat(Thing.one, hasToString("one"));
    }

    private static interface Thing {
        Key<Thing, Integer> one = Key.named("one");
        Key<Thing, Integer> two = Key.named("two");

        Key<?, ?>[] keys = new Key<?, ?>[] {one, two};
    }
}
