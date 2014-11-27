package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.testobjects.TestRekords.Box;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ForwardingKeyTest {
    private static final Key<Person, String> givenName = new TestForwardingKey<>(Person.firstName);
    private static final Key<Box, Boolean> thing = new TestForwardingKey<>(Box.fact);

    @Test public void
    works_as_if_it_were_the_underlying_key_for_storage() {
        Rekord<Person> josephSmith = Person.rekord
                .with(givenName, "Joseph")
                .with(Person.lastName, "Smith");

        assertThat(josephSmith.get(Person.firstName), is("Joseph"));
    }

    @Test public void
    works_as_if_it_were_the_underlying_key_for_retrieval() {
        Rekord<Person> josephSmith = Person.rekord
                .with(Person.firstName, "Sachin")
                .with(Person.lastName, "Tendulkar");

        assertThat(josephSmith.get(givenName), is("Sachin"));
    }

    @Test public void
    tests_presence_as_if_it_were_the_underlying_key() {
        Rekord<Box> box = Box.rekord
                .with(Box.fact, true);

        assertThat(box.has(thing), is(true));
    }

    @Test public void
    tests_absence_as_if_it_were_the_underlying_key() {
        Rekord<Box> box = Box.rekord
                .with(Box.number, 9);

        assertThat(box.has(thing), is(false));
    }

    private static final class TestForwardingKey<T, V> extends ForwardingKey<T, V> {
        public TestForwardingKey(Key<T, V> key) {
            super(key.name(), key);
        }
    }
}
