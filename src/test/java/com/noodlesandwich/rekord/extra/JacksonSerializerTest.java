package com.noodlesandwich.rekord.extra;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Company;
import static com.noodlesandwich.rekord.testobjects.Rekords.LegoBag;
import static com.noodlesandwich.rekord.testobjects.Rekords.LegoBag.Brick;
import static com.noodlesandwich.rekord.testobjects.Rekords.Person;

public final class JacksonSerializerTest {
    @Test public void
    serializes_to_JSON() throws JSONException, IOException {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Douglas")
                .with(Person.lastName, "Crockford");

        StringWriter writer = new StringWriter();
        person.serialize(new JacksonSerializer(writer));

        JSONAssert.assertEquals("{\"first name\": \"Douglas\", \"last name\": \"Crockford\"}", writer.toString(), true);
    }

    @Test public void
    serializes_nested_objects_to_JSON() throws JSONException, IOException {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Douglas")
                .with(Person.lastName, "Crockford")
                .with(Person.pets, ImmutableList.of("Bob"))
                .with(Person.company, Company.rekord
                        .with(Company.name, "PayPal")
                        .with(Company.address, Address.rekord
                                .with(Address.city, "San Jose")));

        StringWriter writer = new StringWriter();
        person.serialize(new JacksonSerializer(writer));

        JSONObject expected = new JSONObject()
                .put("first name", "Douglas")
                .put("last name", "Crockford")
                .put("pets", new JSONArray().put("Bob"))
                .put("company", new JSONObject()
                        .put("name", "PayPal")
                        .put("address", new JSONObject()
                                .put("city", "San Jose")));

        JSONAssert.assertEquals(expected, new JSONObject(writer.toString()), true);
    }

    @Test public void
    serializes_nested_collections_to_JSON() throws JSONException, IOException {
        Rekord<LegoBag> lego = LegoBag.rekord
                .with(LegoBag.sets, ImmutableList.<Collection<Brick>>of(
                        ImmutableList.of(Brick.Green, Brick.Red),
                        ImmutableList.of(Brick.Red, Brick.Blue, Brick.Red)))
                .with(LegoBag.minifigs, ImmutableList.<FixedRekord<Person>>of(
                        Person.rekord.with(Person.firstName, "Indiana").with(Person.lastName, "Jones"),
                        Person.rekord.with(Person.firstName, "Luke").with(Person.lastName, "Skywalker"),
                        Person.rekord.with(Person.firstName, "Batman")));

        StringWriter writer = new StringWriter();
        lego.serialize(new JacksonSerializer(writer));

        JSONObject expected = new JSONObject()
                .put("lego sets", new JSONArray()
                        .put(new JSONArray().put("Green").put("Red"))
                        .put(new JSONArray().put("Red").put("Blue").put("Red")))
                .put("minifigs", new JSONArray()
                        .put(new JSONObject().put("first name", "Indiana").put("last name", "Jones"))
                        .put(new JSONObject().put("first name", "Luke").put("last name", "Skywalker"))
                        .put(new JSONObject().put("first name", "Batman")));

        JSONAssert.assertEquals(expected, new JSONObject(writer.toString()), true);
    }
}
