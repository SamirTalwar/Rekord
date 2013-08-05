package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.RekordType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class NamedKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties<Thing> properties = new Properties<Thing>()
                .with(Thing.one, 1)
                .with(Thing.two, 2);

        assertThat(Thing.one.retrieveFrom(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties<Thing> properties = new Properties<Thing>()
                .with(Thing.one, 5);

        assertThat(Thing.two.retrieveFrom(properties), is(nullValue()));
    }

    @Test public void
    stringifies_to_its_name() {
        assertThat(Thing.one, hasToString("one"));
    }

    private static interface Thing extends RekordType {
        Key<Thing, Integer> one = Key.named("one");
        Key<Thing, Integer> two = Key.named("two");
    }
}
