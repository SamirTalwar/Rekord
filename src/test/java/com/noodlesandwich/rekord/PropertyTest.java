package com.noodlesandwich.rekord;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;

public final class PropertyTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    the_key_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(allOf(instanceOf(NullPointerException.class),
                hasProperty("message", equalTo("Cannot construct a Rekord property with a null key."))));

        new Property<>(null, "Random value");
    }

    @Test public void
    the_value_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(allOf(instanceOf(NullPointerException.class),
                hasProperty("message", equalTo("Cannot construct a Rekord property with a null value."))));

        new Property<>(Key.named("Random key"), null);
    }
}
