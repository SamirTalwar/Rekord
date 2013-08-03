package com.noodlesandwich.rekord;

import org.junit.Test;

import static com.noodlesandwich.rekord.RekordTest.Filling.Cheese;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RekordTest {
    @Test public void
    a_Rekord_contains_a_value() {
        Rekord<Sandvich> sandvich = Rekord.<Sandvich>create()
                .with(Sandvich.filling, Cheese)
                .build();
        assertThat(sandvich.get(Sandvich.filling), is(Cheese));
    }

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = new Key<>();
    }

    public static enum Filling {
        Cheese
    }
}
