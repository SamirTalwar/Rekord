package com.noodlesandwich.rekord.keys;

import java.util.Map;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.Rekords;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.Brown;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Ham;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Jam;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Sandvich.Filling.Lettuce;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class DelegatingKeysTest {
    @Test public void
    a_rekord_is_aware_of_the_underlying_keys_even_when_constructed_with_wrapping_keys() {
        Key<Sandvich, Bread> bread = DefaultedKey.wrapping(Sandvich.bread).defaultingTo(Brown);
        Key<Sandvich, Filling> filling = FunctionKey.wrapping(Sandvich.filling).with(rotatedFillings());
        Rekord<Sandvich> sandvichRekord = Rekords.of(Sandvich.class)
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

            private final Map<Filling, Filling> forwardRota = rota;
            private final Map<Filling, Filling> backwardRota = rota.inverse();

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
