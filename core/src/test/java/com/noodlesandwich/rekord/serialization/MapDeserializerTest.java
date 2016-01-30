package com.noodlesandwich.rekord.serialization;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
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
}
