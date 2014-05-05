package com.noodlesandwich.rekord.serialization;

import com.google.common.collect.ImmutableList;
import com.noodlesandwich.rekord.Rekord;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.startsWith;

public final class StringSerializerTest {
    @Test public void
    serializes_an_empty_rekord_to_a_string_just_containing_its_name() {
        Rekord<Person> person = Person.rekord;
        assertThat(person, hasToString("Person {}"));
    }

    @Test public void
    serializes_a_rekord_with_a_single_entry_to_a_string_with_that_entry() {
        Rekord<Person> lisa = Person.rekord
                .with(Person.firstName, "Homer");
        assertThat(lisa, hasToString("Person {first name: Homer}"));
    }

    @Test public void
    serializes_a_rekord_with_multiple_entries_to_a_comma_delimited_string() {
        Rekord<Person> ned = Person.rekord
                .with(Person.firstName, "Ned")
                .with(Person.lastName, "Flanders");
        assertThat(ned, either(hasToString("Person {first name: Ned, last name: Flanders}"))
                           .or(hasToString("Person {last name: Flanders, first name: Ned}")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    serializes_nested_rekords() {
        Rekord<Person> maggie = Person.rekord
                .with(Person.firstName, "Maggie")
                .with(Person.lastName, "Simpson");

        Rekord<Person> marge = Person.rekord
                .with(Person.firstName, "Marge")
                .with(Person.lastName, "Simpson");

        Rekord<Person> lisa = Person.rekord
                .with(Person.firstName, "Lisa")
                .with(Person.lastName, "Simpson")
                .with(Person.favouritePeople, ImmutableList.of(maggie, marge))
                .with(Person.address, Address.rekord
                        .with(Address.city, "Springfield"));

        assertThat(lisa, hasToString(allOf(
                startsWith("Person {"),
                containsString("first name: Lisa"),
                containsString("last name: Simpson"),
                anyOf(containsString("favourite people: [Person {first name: Maggie, last name: Simpson}, Person {first name: Marge, last name: Simpson}]"),
                      containsString("favourite people: [Person {first name: Maggie, last name: Simpson}, Person {last name: Simpson, first name: Marge}]"),
                      containsString("favourite people: [Person {last name: Simpson, first name: Maggie}, Person {first name: Marge, last name: Simpson}]"),
                      containsString("favourite people: [Person {last name: Simpson, first name: Maggie}, Person {last name: Simpson, first name: Marge}]")),
                containsString("address: Address {city: Springfield}"),
                endsWith("}")
        )));
    }
}
