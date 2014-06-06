package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RenamedKeyTest {
    private static final Key<Person, String> givenName = RenamedKey.renaming(Person.firstName).to("given name");

    @Test public void
    renames_keys() {
        assertThat(givenName.name(), is("given name"));
    }

    @Test public void
    works_as_if_it_were_the_underlying_key() {
        Rekord<Person> josephSmith = Person.rekord
                .with(givenName, "Joseph")
                .with(Person.lastName, "Smith");

        assertThat(josephSmith.get(givenName), is("Joseph"));
    }
}
