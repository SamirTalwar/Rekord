package com.noodlesandwich.rekord.keys;

import java.util.Map;
import com.noodlesandwich.rekord.Key;
import org.junit.Test;
import org.pcollections.HashPMap;
import org.pcollections.HashTreePMap;

import static com.noodlesandwich.rekord.Rekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class OrdinaryKeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Map<Key<? super Person, ?>, Object> properties = properties()
                .plus(Person.firstName, "Johannes")
                .plus(Person.age, 25);

        assertThat(Person.firstName.retrieveFrom(properties), is("Johannes"));
    }

    @Test public void
    returns_null_if_the_property_map_does_not_contain_the_key() {
        Map<Key<? super Person, ?>, Object> properties = properties()
                .plus(Person.firstName, "Andreas")
                .plus(Person.age, 30);

        assertThat(Person.lastName.retrieveFrom(properties), is(nullValue()));
    }

    @Test public void
    states_whether_a_key_is_contained_in_a_property_map() {
        Map<Key<? super Person, ?>, Object> properties = properties()
                .plus(Person.firstName, "Jan")
                .plus(Person.age, 40);

        assertThat(Person.firstName.isContainedIn(properties), is(true));
        assertThat(Person.lastName.isContainedIn(properties), is(false));
    }

    private static HashPMap<Key<? super Person, ?>, Object> properties() {
        return HashTreePMap.empty();
    }
}
