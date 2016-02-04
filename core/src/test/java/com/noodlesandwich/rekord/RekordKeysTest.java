package com.noodlesandwich.rekord;

import java.util.Collections;
import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.UnacceptableKeyException;
import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.test.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Wurst;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class RekordKeysTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_Rekord_can_tell_which_keys_are_being_used() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Jam)
                .with(Sandvich.bread, Brown);

        assertThat(sandvich.has(Sandvich.filling), is(true));
        assertThat(sandvich.has(Sandvich.bread), is(true));
        assertThat(sandvich.has(Sandvich.style), is(false));
    }

    @Test public void
    a_Rekord_must_know_all_possible_keys_in_advance() {
        expectedException.expect(an(UnacceptableKeyException.class)
                .withTheMessage("The key \"spice\" is not a valid key for this Rekord."));

        Key<Wurst, Integer> spice = SimpleKey.named("spice");

        Rekords.of(Wurst.class).accepting(Wurst.curvature)
                .with(spice, 7);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    the_keys_of_the_properties_of_a_Rekord_can_be_retrieved() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.keys(), Matchers
                .<Key<Sandvich, ?>>containsInAnyOrder(Sandvich.filling, Sandvich.style));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    whether_a_property_is_present_is_up_to_the_key() {
        Key<Sandvich, Object> missingKey = new BrokenKey<>();
        Rekord<Sandvich> sandvichRekord = Rekords.of(Sandvich.class)
                .accepting(Sandvich.rekord.acceptedKeys(), missingKey);

        Rekord<Sandvich> sandvich = sandvichRekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.bread, White)
                .with(missingKey, "Boop.");

        assertThat(sandvich.keys(), Matchers
                .<Key<Sandvich, ?>>containsInAnyOrder(Sandvich.filling, Sandvich.bread));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    a_rekord_can_list_its_allowed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.acceptedKeys(), Matchers
                .<Key<Sandvich, ?>>containsInAnyOrder(Sandvich.bread, Sandvich.filling, Sandvich.style));
    }

    private static final class BrokenKey<T, V> extends AbstractKey<T, V> {
        public BrokenKey() {
            super("<broken>");
        }

        @Override
        public boolean test(Properties<T> properties) {
            return false;
        }

        @Override
        public V get(Properties<T> properties) {
            return null;
        }

        @Override
        public Properties<T> set(V value, Properties<T> properties) {
            return properties;
        }

        @Override
        public <A, E extends Exception> void serialize(V value, Serializer.Accumulator<A, E> accumulator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <R, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, R, E> accumulator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Key<T, ?>> iterator() {
            return Collections.<Key<T, ?>>singleton(this).iterator();
        }
    }
}
