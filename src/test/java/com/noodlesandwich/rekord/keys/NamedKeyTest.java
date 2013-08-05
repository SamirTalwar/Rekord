package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Properties;
import org.junit.Test;

import static com.noodlesandwich.rekord.Rekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class NamedKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Properties<Person> properties = new Properties<Person>()
                .with(Person.firstName, "Johannes")
                .with(Person.age, 25);

        assertThat(Person.firstName.retrieveFrom(properties), is("Johannes"));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Properties<Person> properties = new Properties<Person>()
                .with(Person.firstName, "Andreas")
                .with(Person.age, 30);

        assertThat(Person.lastName.retrieveFrom(properties), is(nullValue()));
    }

    @Test public void
    stringifies_to_its_name() {
        assertThat(Person.firstName, hasToString("first name"));
    }
}
