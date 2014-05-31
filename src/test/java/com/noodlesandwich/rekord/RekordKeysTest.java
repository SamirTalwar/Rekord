package com.noodlesandwich.rekord;

import java.util.Map;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.keys.DefaultedKey;
import com.noodlesandwich.rekord.keys.FunctionKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.SimpleKey;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Lettuce;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Flat;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.testobjects.Rekords.Wurst;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class RekordKeysTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    @Test public void
    a_Rekord_can_tell_which_keys_are_being_used() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Jam)
                .with(Sandvich.bread, Brown);

        assertThat(sandvich, allOf(
                hasProperties(Sandvich.filling, Sandvich.bread),
                not(hasProperties(Sandvich.style))));
    }

    @Test public void
    a_Rekord_must_know_all_possible_keys_in_advance() {
        expectedException.expect(an(IllegalArgumentException.class)
                .withTheMessage("The key \"spice\" is not a valid key for this Rekord."));

        Key<Wurst, Integer> spice = SimpleKey.named("spice");

        Rekord.of(Bratwurst.class).accepting(Wurst.curvature, Bratwurst.style)
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
    a_rekord_can_list_its_allowed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.acceptedKeys(), Matchers
                .<Key<? super Sandvich, ?>>containsInAnyOrder(Sandvich.bread, Sandvich.filling, Sandvich.style));
    }

    @Test public void
    a_rekord_knows_whether_it_has_a_property_with_a_specific_key() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Jam)
                .with(Sandvich.style, Flat);

        assertThat(sandvich, hasKey(Sandvich.filling));
        assertThat(sandvich, not(hasKey(Sandvich.bread)));
        assertThat(sandvich, hasKey(Sandvich.style));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    a_rekord_can_work_with_keys_of_a_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.9)
                .with(Bratwurst.style, Chopped);

        assertThat(bratwurst, hasKey(Wurst.curvature));
        assertThat(bratwurst.keys(), Matchers.<Key<? super Bratwurst, ?>>
                containsInAnyOrder(Wurst.curvature, Bratwurst.style));
    }

    @Test public void
    a_rekord_is_aware_of_the_underlying_keys_even_when_constructed_with_wrapping_keys() {
        Key<Sandvich, Bread> bread = DefaultedKey.wrapping(Sandvich.bread).defaultingTo(Brown);
        Key<Sandvich, Filling> filling = FunctionKey.wrapping(Sandvich.filling).with(rotatedFillings());
        Rekord<Sandvich> sandvichRekord = Rekord.of(Sandvich.class)
                .accepting(bread, filling, Sandvich.style);

        Rekord<Sandvich> sandvich = sandvichRekord
                .with(Sandvich.bread, White)
                .with(Sandvich.filling, Ham);

        assertThat(sandvich.get(bread), is(White));
        assertThat(sandvich.get(filling), is(Jam));
    }

    private static InvertibleFunction<Filling, Filling> rotatedFillings() {
        return new InvertibleFunction<Filling, Filling>() {
            private final BiMap<Filling, Filling> rota = HashBiMap.create(ImmutableMap.of(
                Cheese, Ham,
                Ham, Jam,
                Jam, Lettuce,
                Lettuce, Cheese
            ));

            Map<Filling, Filling> forwardRota = rota;
            Map<Filling, Filling> backwardRota = rota.inverse();

            @Override
            public Filling applyForward(Filling filling) {
                return forwardRota.get(filling);
            }

            @Override
            public Filling applyBackward(Filling filling) {
                return backwardRota.get(filling);
            }
        };
    }
}
