package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.RekordType;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

public class DefaultedKeyTest {
    @Test
    public void
    retrieves_a_value_from_a_property_map() {
        Properties<Event> properties = new Properties<Event>()
                .with(Event.what.of("Dance-off"))
                .with(Event.where.of("London"));

        assertThat(Event.where.retrieveFrom(properties), is("London"));
    }

    @Test public void
    returns_the_default_value_if_the_property_map_does_not_contain_the_key() {
        Properties<Event> properties = new Properties<Event>()
                .with(Event.where.of("San Francisco"));

        assertThat(Event.what.retrieveFrom(properties), is("That"));
    }

    @Test public void
    stringifies_to_its_name() {
        assertThat(Event.what, hasToString("what"));
    }

    private static interface Event extends RekordType {
        Key<Event, String> what = Key.<Event, String>named("what").defaultingTo("That");
        Key<Event, String> where = Key.<Event, String>named("where").defaultingTo("There");
    }
}
