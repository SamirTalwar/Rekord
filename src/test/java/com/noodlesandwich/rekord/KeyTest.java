package com.noodlesandwich.rekord;

import java.util.Map;
import org.junit.Test;
import org.pcollections.HashTreePMap;

import static com.noodlesandwich.rekord.Rekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class KeyTest {
    @Test public void
    retrieves_a_value_from_a_property_map() {
        Map<Key<? super Person, ?>, Object> properties = HashTreePMap.<Key<? super Person, ?>, Object>empty()
                .plus(Person.firstName, "Johannes")
                .plus(Person.age, 25);

        assertThat(Person.firstName.retrieveFrom(properties), is("Johannes"));
    }
}
