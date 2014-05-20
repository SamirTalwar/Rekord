package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.keys.TransformingKey;
import com.noodlesandwich.rekord.transformers.Transformer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.a;

public final class TransformingPropertyTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    the_key_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("Cannot construct a Rekord property with a null key."));

        new TransformingProperty<>(null, "Random value", BROKEN_TRANSFORMER);
    }

    @Test public void
    the_value_of_a_Rekord_property_cannot_be_null() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("Cannot construct a Rekord property with a null value."));

        TransformingKey<Object, Object, Object> key = new TransformingKey<>(SimpleKey.named("Random key"), BROKEN_TRANSFORMER);
        new TransformingProperty<>(key, null, BROKEN_TRANSFORMER);
    }

    public static final Transformer<Object, Object> BROKEN_TRANSFORMER = new Transformer<Object, Object>() {
        @Override
        public Object transformInput(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object transformOutput(Object value) {
            throw new UnsupportedOperationException();
        }
    };
}
