package com.noodlesandwich.rekord.keys;

import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RenamedKeyTest {
    private static final Key<Person, String> givenName = RenamedKey.renaming(Person.firstName).to("given name");

    @Test public void
    renames_keys() {
        assertThat(givenName.name(), is("given name"));
    }
}
