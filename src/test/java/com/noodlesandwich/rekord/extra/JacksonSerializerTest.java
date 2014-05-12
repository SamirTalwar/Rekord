package com.noodlesandwich.rekord.extra;

import java.io.IOException;
import java.io.StringWriter;
import com.noodlesandwich.rekord.Rekord;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
}
