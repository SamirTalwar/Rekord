package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.testobjects.TestRekords.Box;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class RenamedKeyTest {
    private static final Key<Person, String> givenName = RenamedKey.renaming(Person.firstName).to("given name");
    private static final Key<Box, Boolean> thing = RenamedKey.renaming(Box.fact).to("thing");

    @Test public void
    renames_keys() {
        assertThat(givenName.name(), is("given name"));
    }

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

        assertThat(box, hasKey(thing));
    }

    @Test public void
    tests_absence_as_if_it_were_the_underlying_key() {
        Rekord<Box> box = Box.rekord
                .with(Box.number, 9);

        assertThat(box, not(hasKey(thing)));
    }
}
