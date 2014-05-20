package com.noodlesandwich.rekord.extra;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
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
import static com.noodlesandwich.rekord.testobjects.Rekords.Box;
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
        person.serialize(JacksonSerializer.serializingToWriter(writer));
        writer.close();

        JSONAssert.assertEquals("{\"first name\": \"Douglas\", \"last name\": \"Crockford\"}", writer.toString(), true);
    }

    @Test public void
    serializes_other_primitives() throws JSONException, IOException {
        Rekord<Box> box = Box.rekord
                .with(Box.fact, true)
                .with(Box.number, 7)
                .with(Box.real, -1.5)
                .with(Box.text, "Hiya")
                .with(Box.stuff, ImmutableList.<Object>of(false, 99, 100.001, "Boo"));

        StringWriter writer = new StringWriter();
        box.serialize(JacksonSerializer.serializingToWriter(writer));

        JSONAssert.assertEquals("{" +
            "\"fact\": true, " +
            "\"number\": 7, " +
            "\"real\": -1.5, " +
            "\"text\": \"Hiya\", " +
            "\"stuff\": [false, 99, 100.001, \"Boo\"]" +
        "}", writer.toString(), true);
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
        person.serialize(JacksonSerializer.serializingToWriter(writer));

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
        lego.serialize(JacksonSerializer.serializingToWriter(writer));

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

    @Test public void
    is_capable_of_serializing_to_a_string() throws JSONException, IOException {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Douglas")
                .with(Person.lastName, "Crockford");

        String json = person.serialize(JacksonSerializer.serializingToString());

        JSONAssert.assertEquals("{\"first name\": \"Douglas\", \"last name\": \"Crockford\"}", json, true);
    }

    @Test public void
    lets_the_owner_of_the_writer_close_it() throws JSONException, IOException {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Douglas")
                .with(Person.lastName, "Crockford");

        ClosingStringWriter writer = new ClosingStringWriter();

        person.serialize(JacksonSerializer.serializingToWriter(writer));
        person.serialize(JacksonSerializer.serializingToWriter(writer));

        JSONAssert.assertEquals(
                "{\"first name\": \"Douglas\", \"last name\": \"Crockford\"}" +
                "{\"first name\": \"Douglas\", \"last name\": \"Crockford\"}",
                writer.toString(),
                true);
    }

    private static final class ClosingStringWriter extends Writer {
        private final StringWriter delegate = new StringWriter();
        private boolean open = true;

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            checkOpen();
            delegate.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            checkOpen();
            delegate.flush();
        }

        @Override
        public void close() {
            this.open = false;
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        private void checkOpen() throws IOException {
            if (!open) {
                throw new IOException("Writer already closed.");
            }
        }
    }
}
