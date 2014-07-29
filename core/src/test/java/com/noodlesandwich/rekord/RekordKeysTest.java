package com.noodlesandwich.rekord;

import java.util.Collections;
import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.UnacceptableKeyException;
import com.noodlesandwich.rekord.serialization.Serializer;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.test.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Wurst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RekordKeysTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final Key<Object, Object> missingKey = new BrokenKey<>();

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

        Rekords.of(Bratwurst.class).accepting(Wurst.curvature, Bratwurst.style)
                .with(spice, 7);
    }

    @SuppressWarnings("unchecked")
    @Test public void
    the_keys_of_the_properties_of_a_Rekord_can_be_retrieved() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.keys(), Matchers
                .<Key<? super Sandvich, ?>>containsInAnyOrder(Sandvich.filling, Sandvich.style));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    whether_a_property_is_present_is_up_to_the_key() {
        Rekord<Bratwurst> bratwurstRekord = Rekords.of(Bratwurst.class)
                .accepting(Bratwurst.rekord.acceptedKeys(), missingKey);

        Rekord<Bratwurst> bratwurst = bratwurstRekord
                .with(Wurst.curvature, 0.5)
                .with(Bratwurst.style, Chopped)
                .with(missingKey, "Boop.");

        assertThat(bratwurst.keys(), Matchers
                .<Key<? super Bratwurst, ?>>containsInAnyOrder(Wurst.curvature, Bratwurst.style));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    a_rekord_can_list_its_allowed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.acceptedKeys(), Matchers
                .<Key<? super Sandvich, ?>>containsInAnyOrder(Sandvich.bread, Sandvich.filling, Sandvich.style));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    a_rekord_can_work_with_keys_of_a_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.9)
                .with(Bratwurst.style, Chopped);

        assertThat(bratwurst.has(Wurst.curvature), is(true));
        assertThat(bratwurst.keys(), Matchers.<Key<? super Bratwurst, ?>>
                containsInAnyOrder(Wurst.curvature, Bratwurst.style));
    }

    private static final class BrokenKey<T, V> extends AbstractKey<T, V> {
        public BrokenKey() {
            super("<broken>");
        }

        @Override
        public <R extends T> boolean test(Properties<R> properties) {
            return false;
        }

        @Override
        public <R extends T> V get(Properties<R> properties) {
            return null;
        }

        @Override
        public <R extends T> Properties<R> set(V value, Properties<R> properties) {
            return properties;
        }

        @Override
        public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Key<? super T, ?>> iterator() {
            return Collections.<Key<? super T, ?>>singleton(this).iterator();
        }
    }
}
