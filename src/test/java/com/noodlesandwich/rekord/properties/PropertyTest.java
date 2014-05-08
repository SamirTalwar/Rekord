package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.SimpleKey;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.a;

public final class PropertyTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    the_key_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("Cannot construct a Rekord property with a null key."));
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("Cannot construct a Rekord property with a null key."));

        new Property(null, "Random value");
    }

    @Test public void
    the_value_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("Cannot construct a Rekord property with a null value."));

        new Property(SimpleKey.named("Random key"), null);
    }
}
