package com.noodlesandwich.rekord.serialization;

import java.util.Map;
import java.util.Set;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;
import com.noodlesandwich.rekord.testobjects.TestRekords.LegoBag.Brick;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.LegoBag;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Person;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapDeserializerTest {
    private final MapDeserializer deserializer = new MapDeserializer();

    @Test public void
    deserializes_a_rekord_to_a_map_of_strings_to_objects() throws KeyNotFoundException {
        Rekord<Sandvich> rekord = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Roll);
        Map<String, Object> map = ImmutableMap.<String, Object>of(
                "bread", White,
                "style", Roll);

        Rekord<Sandvich> deserialized = Rekords.deserialize(map).into(Sandvich.rekord).with(deserializer);

        assertThat(deserialized, is(equalTo(rekord)));
    }

    @Test public void
    deserializes_nested_rekords() throws KeyNotFoundException {
        Rekord<Person> rekord = Person.rekord
                .with(Person.firstName, "Queen Elizabeth")
                .with(Person.lastName, "II")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 1)
                        .with(Address.street, "The Mall")
                        .with(Address.city, "London")
                        .with(Address.postalCode, "SW1 1AA"));
        Map<String, Object> map = ImmutableMap.<String, Object>of(
                "first name", "Queen Elizabeth",
                "last name", "II",
                "address", ImmutableMap.<String, Object>of(
                        "house number", 1,
                        "street", "The Mall",
                        "city", "London",
                        "postal code", "SW1 1AA"));

        Rekord<Person> deserialized = Rekords.deserialize(map).into(Person.rekord).with(deserializer);

        assertThat(deserialized, is(equalTo(rekord)));
    }

    @Test public void
    deserializes_collections() throws KeyNotFoundException {
        Rekord<Person> rekord = Person.rekord
                .with(Person.firstName, "Queen Elizabeth")
                .with(Person.lastName, "II")
                .with(Person.favouritePeople, ImmutableSet.of(
                        Person.rekord.with(Person.firstName, "William"),
                        Person.rekord.with(Person.firstName, "Harry")))
                .with(Person.pets, ImmutableList.of(
                        "Corgi #1",
                        "Corgi #2",
                        "Corgi #3"));
        Map<String, Object> map = ImmutableMap.<String, Object>of(
                "first name", "Queen Elizabeth",
                "last name", "II",
                "favourite people", ImmutableList.of(
                        ImmutableMap.<String, Object>of("first name", "William"),
                        ImmutableMap.<String, Object>of("first name", "Harry")
                ),
                "pets", ImmutableList.of("Corgi #1", "Corgi #2", "Corgi #3"));

        Rekord<Person> deserialized = Rekords.deserialize(map).into(Person.rekord).with(deserializer);

        assertThat(deserialized, is(equalTo(rekord)));
    }

    @Test public void
    deserializes_nested_collections() throws KeyNotFoundException {
        Rekord<LegoBag> rekord = LegoBag.rekord
                .with(LegoBag.sets, ImmutableList.<Set<Brick>>of(
                        ImmutableSet.of(Brick.Red, Brick.Green),
                        ImmutableSet.of(Brick.Green, Brick.Blue)
                ));
        Map<String, Object> map = ImmutableMap.<String, Object>of(
                "lego sets", ImmutableList.of(
                        ImmutableList.of(Brick.Red, Brick.Green),
                        ImmutableList.of(Brick.Green, Brick.Blue)
                ));

        Rekord<LegoBag> deserialized = Rekords.deserialize(map).into(LegoBag.rekord).with(deserializer);

        assertThat(deserialized, is(equalTo(rekord)));
    }
}
