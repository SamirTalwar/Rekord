package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import org.junit.Test;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class SimpleKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties properties = new Properties(Thing.keys);
        properties = Thing.one.storeTo(properties, 1);
        properties = Thing.two.storeTo(properties, 2);

        assertThat(Thing.one.retrieveFrom(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties properties = new Properties(Thing.keys);
        properties = Thing.one.storeTo(properties, 5);

        assertThat(Thing.two.retrieveFrom(properties), is(nullValue()));
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

        PSet<Key<?, ?>> keys = OrderedPSet.<Key<?, ?>>empty().plus(one).plus(two);
    }
}
