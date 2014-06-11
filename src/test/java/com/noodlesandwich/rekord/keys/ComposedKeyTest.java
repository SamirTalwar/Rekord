package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class ComposedKeyTest {
    @Test public void
    composes_a_RekordKey_with_another_key() {
        Rekord<Person> heinz = Person.rekord
                .with(Person.firstName, "Heinz")
                .with(Person.city, "Heraklion");

        assertThat(heinz.get(Person.address).get(Address.city), is("Heraklion"));
    }

    @Test public void
    retrieves_values_using_the_composed_key() {
        Rekord<Person> martijn = Person.rekord
                .with(Person.firstName, "Martijn")
                .with(Person.address, Address.rekord
                        .with(Address.city, "London"));

        assertThat(martijn.get(Person.city), is("London"));
    }

    @Test public void
    tests_as_present_when_both_keys_are() {
        Rekord<Person> ben = Person.rekord
                .with(Person.firstName, "Ben")
                .with(Person.address, Address.rekord
                        .with(Address.city, "London"));

        assertThat(ben, hasKey(Person.city));
    }

    @Test public void
    tests_as_absent_when_the_after_key_is_not_present() {
        Rekord<Person> brian = Person.rekord
                .with(Person.firstName, "Brian")
                .with(Person.address, Address.rekord);

        assertThat(brian, not(hasKey(Person.city)));
    }
}
