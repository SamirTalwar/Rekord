package com.noodlesandwich.rekord.serialization;

import javax.xml.parsers.ParserConfigurationException;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import org.junit.Test;
import org.w3c.dom.Document;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.XmlMatchers.isSimilarTo;
import static org.xmlmatchers.transform.XmlConverters.the;

public final class DomXmlSerializerTest {
    @Test public void
    a_rekord_with_one_element_is_serialized_to_XML() throws ParserConfigurationException {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese);

        Document document = sandvich.serialize(new DomXmlSerializer());

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sandvich>",
                "    <filling>Cheese</filling>",
                "</sandvich>"
        ))));
    }

    @Test public void
    a_rekord_with_multiple_elements_is_serialized_to_XML() throws ParserConfigurationException {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, Brown)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        Document document = sandvich.serialize(new DomXmlSerializer());

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sandvich>",
                "    <bread>Brown</bread>",
                "    <filling>Cheese</filling>",
                "    <style>Roll</style>",
                "</sandvich>"
        ))));
    }

    @Test public void
    rekord_and_key_names_are_lowercased_and_slugified_into_valid_XML_element_names() throws ParserConfigurationException {
        Key<Person, Integer> key_12345 = SimpleKey.named("12345");
        Key<Person, String> key_up = SimpleKey.named("^up");

        Rekord<Person> modifiedPersonRekord = Rekords.<Person>create("sea creaTURE")
                .accepting(Person.firstName, Person.lastName, Person.age, key_12345, key_up);

        Rekord<Person> spongebob = modifiedPersonRekord
                .with(Person.firstName, "Spongebob")
                .with(Person.lastName, "Squarepants")
                .with(Person.age, 27)
                .with(key_12345, 67890)
                .with(key_up, "down");

        Document document = spongebob.serialize(new DomXmlSerializer());

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<sea-creature>",
                "    <first-name>Spongebob</first-name>",
                "    <last-name>Squarepants</last-name>",
                "    <age>27</age>",
                "    <_12345>67890</_12345>",
                "    <_up>down</_up>",
                "</sea-creature>"
        ))));
    }

    @Test public void
    a_rekord_with_nested_rekords_is_serialized_to_nested_XML() throws ParserConfigurationException {
        FixedRekord<Person> darla = Person.rekord.with(Person.firstName, "Darla");
        FixedRekord<Person> darlasMum = Person.rekord.with(Person.firstName, "Darla's Mum");
        FixedRekord<Person> person = Person.rekord
                .with(Person.firstName, "Philip")
                .with(Person.lastName, "Sherman")
                .with(Person.favouritePeople, ImmutableList.of(darla, darlasMum))
                .with(Person.pets, ImmutableList.of("Nemo", "Gill", "Bloat"))
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 42)
                        .with(Address.street, "Wallaby Way")
                        .with(Address.city, "Sydney"));

        Document document = person.serialize(new DomXmlSerializer());

        assertThat(the(document), isSimilarTo(the(lines(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
                "<person>",
                "    <first-name>Philip</first-name>",
                "    <last-name>Sherman</last-name>",
                "    <favourite-people>",
                "        <favourite-person><first-name>Darla</first-name></favourite-person>",
                "        <favourite-person><first-name>Darla's Mum</first-name></favourite-person>",
                "    </favourite-people>",
                "    <pets>",
                "        <pet>Nemo</pet>",
                "        <pet>Gill</pet>",
                "        <pet>Bloat</pet>",
                "    </pets>",
                "    <address>",
                "        <house-number>42</house-number>",
                "        <street>Wallaby Way</street>",
                "        <city>Sydney</city>",
                "    </address>",
                "</person>"
        ))));
    }

    private static String lines(String... lines) {
        return Joiner.on('\n').join(lines);
    }
}
