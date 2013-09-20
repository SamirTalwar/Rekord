package com.noodlesandwich.rekord;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static com.noodlesandwich.rekord.Rekords.Sandvich;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Style.Roll;
import static com.noodlesandwich.rekord.matchers.RekordMatchers.hasKey;

public final class RekordKeysTest {
    @Test public void
    a_Rekord_can_tell_which_keys_are_being_used() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Jam)
                .with(Sandvich.bread, Brown);

        assertThat(sandvich, allOf(
                hasKey(Sandvich.filling),
                hasKey(Sandvich.bread),
                not(hasKey(Sandvich.style))));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    the_keys_of_the_properties_of_a_Rekord_can_be_retrieved() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Roll);

        assertThat(sandvich.keys(), Matchers
                .<Key<? super Sandvich, ?>>containsInAnyOrder(Sandvich.filling, Sandvich.style));
    }
}
