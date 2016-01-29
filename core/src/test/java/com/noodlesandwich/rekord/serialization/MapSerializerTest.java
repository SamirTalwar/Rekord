package com.noodlesandwich.rekord.serialization;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.LegoBag;
import static com.noodlesandwich.rekord.testobjects.TestRekords.LegoBag.Brick;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapSerializerTest {
    @Test public void
    serializes_and_deserializes_a_rekord_to_a_map_of_strings_to_objects() {
        Map<String, Object> expectedSerialized = ImmutableMap.<String, Object>of(
                "bread", White,
                "style", Roll);
        Rekord<Sandvich> expectedDeserialized = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Roll);

        Rekord<Sandvich> actualDeserialized = Rekords.deserialize(expectedSerialized).into(Sandvich.rekord).with(new MapSerializer());
        assertThat(actualDeserialized, is(equalTo(expectedDeserialized)));

        Map<String, Object> actualSerialized = actualDeserialized.serialize(new MapSerializer());
        assertThat(actualSerialized, is(equalTo(expectedSerialized)));
    }

    @Test public void
    serializes_nested_rekords() {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Queen Elizabeth")
                .with(Person.lastName, "II")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 1)
                        .with(Address.street, "The Mall")
                        .with(Address.city, "London")
                        .with(Address.postalCode, "SW1 1AA"));

        Map<String, Object> serialized = person.serialize(new MapSerializer());

        Map<String, Object> expected = ImmutableMap.<String, Object>of(
                "first name", "Queen Elizabeth",
                "last name", "II",
                "address", ImmutableMap.<String, Object>of(
                        "house number", 1,
                        "street", "The Mall",
                        "city", "London",
                        "postal code", "SW1 1AA"));
        assertThat(serialized, is(equalTo(expected)));
    }

    @Test public void
    serializes_collections() {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Queen Elizabeth")
                .with(Person.lastName, "II")
                .with(Person.favouritePeople, ImmutableList.of(
                        Person.rekord.with(Person.firstName, "William"),
                        Person.rekord.with(Person.firstName, "Harry")))
                .with(Person.pets, ImmutableList.of(
                        "Corgi #1",
                        "Corgi #2",
                        "Corgi #3"));

        Map<String, Object> serialized = person.serialize(new MapSerializer());

        Map<String, Object> expected = ImmutableMap.<String, Object>of(
                "first name", "Queen Elizabeth",
                "last name", "II",
                "favourite people", ImmutableList.of(
                        ImmutableMap.<String, Object>of("first name", "William"),
                        ImmutableMap.<String, Object>of("first name", "Harry")
                ),
                "pets", ImmutableList.of("Corgi #1", "Corgi #2", "Corgi #3"));
        assertThat(serialized, is(equalTo(expected)));
    }

    @Test public void
    serializes_nested_collections() {
        Rekord<LegoBag> lego = LegoBag.rekord
                .with(LegoBag.sets, ImmutableList.<Collection<Brick>>of(
                        ImmutableList.of(Brick.Red, Brick.Green),
                        ImmutableList.of(Brick.Green, Brick.Blue)
                ));

        Map<String, Object> serialized = lego.serialize(new MapSerializer());

        Map<String, Object> expected = ImmutableMap.<String, Object>of(
                "lego sets", ImmutableList.of(
                        ImmutableList.of(Brick.Red, Brick.Green),
                        ImmutableList.of(Brick.Green, Brick.Blue)
                ));
        assertThat(serialized, is(equalTo(expected)));
    }
}
