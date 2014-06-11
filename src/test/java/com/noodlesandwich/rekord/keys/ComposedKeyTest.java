package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class ComposedKeyTest {
    @Test public void
    composes_a_RekordKey_with_another_key() {
        Rekord<Person> heinz = Person.rekord
                .with(Person.firstName, "Heinz")
                .with(Person.city, "Heraklion");

        assertThat(heinz.get(Person.address).get(Address.city), is("Heraklion"));
    }
}
