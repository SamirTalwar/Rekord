package com.noodlesandwich.rekord.keys;

import org.hamcrest.MatcherAssert;
import org.junit.Test;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.RekordType;

import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class NamedKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties properties = new Properties();
        properties = Thing.one.storeTo(properties, 1);
        properties = Thing.two.storeTo(properties, 2);

        MatcherAssert.assertThat(Thing.one.retrieveFrom(properties), is(1));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties properties = new Properties();
        properties = Thing.one.storeTo(properties, 5);

        MatcherAssert.assertThat(Thing.two.retrieveFrom(properties), is(nullValue()));
    }

    @Test public void
    stringifies_to_its_name() {
        MatcherAssert.assertThat(Thing.one, hasToString("one"));
    }

    private static interface Thing extends RekordType {
        Key<Thing, Integer> one = Key.named("one");
        Key<Thing, Integer> two = Key.named("two");
    }
}
