package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.RekordType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class PropertyKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties<Thing> properties = new Properties<Thing>()
                .with(Thing.one.of(1))
                .with(Thing.two.of(2));

        assertThat(Thing.one.retrieveFrom(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties<Thing> properties = new Properties<Thing>()
                .with(Thing.one.of(5));

        assertThat(Thing.two.retrieveFrom(properties), is(nullValue()));
    }

    private static interface Thing extends RekordType {
        Key<Thing, Integer> one = new PropertyKey<>();
        Key<Thing, Integer> two = new PropertyKey<>();
    }
}
