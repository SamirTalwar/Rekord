package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Company;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public final class ComposedKeyTest {
    @Test public void
    composes_a_RekordKey_with_another_key() {
        Rekord<Person> heinz = Person.rekord
                .with(Person.firstName, "Heinz")
                .with(Person.city, "Heraklion");

        assertThat(heinz.get(Person.address).get(Address.city), is("Heraklion"));
    }

    @Test public void
    modifies_an_existing_nested_rekord_instead_of_wiping_it_out() {
        Rekord<Person> remi = Person.rekord
                .with(Person.firstName, "Remi")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 5)
                        .with(Address.street, "boulevard Descartes"))
                .with(Person.city, "Paris");

        assertThat(remi.get(Person.address), is(Address.rekord
                .with(Address.houseNumber, 5)
                .with(Address.street, "boulevard Descartes")
                .with(Address.city, "Paris")));
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
    returns_null_when_the_before_key_is_not_present() {
        Rekord<Person> graham = Person.rekord
                .with(Person.firstName, "Graham");

        assertThat(graham.get(Person.city), is(nullValue()));
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

    @Test public void
    tests_as_absent_when_the_before_key_is_not_present() {
        Rekord<Person> zhong = Person.rekord
                .with(Person.firstName, "Zhong");

        assertThat(zhong, not(hasKey(Person.city)));
    }

    @Test public void
    composes_multiple_levels() {
        BuildableKey<Person, Rekord<Address>> companyAddress
                = ComposedKey.named("company address").composing(Person.company).with(Company.address);
        Key<Person, String> companyCity =
                ComposedKey.named("company city").composing(companyAddress).with(Address.city);

        Rekord<Person> anna = Person.rekord
                .with(Person.firstName, "Anna")
                .with(companyCity, "St. Petersburg");

        assertThat(anna.get(Person.company).get(Company.address).get(Address.city), is("St. Petersburg"));
    }
}
