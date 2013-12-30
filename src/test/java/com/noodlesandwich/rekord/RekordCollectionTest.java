package com.noodlesandwich.rekord;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;
import com.noodlesandwich.rekord.testobjects.Measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static com.noodlesandwich.rekord.Kollector.Accumulator;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Wurst;

public final class RekordCollectionTest {
    private final Mockery context = new Mockery();

    @Test public void
    a_Rekord_can_be_collected_into_a_collector() {
        Rekord<Sandvich> sandvich = Rekord.of(Sandvich.class)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Burger);

        final Accumulator accumulator = accumulator();
        final Kollector<Accumulator, String> kollector = kollector();

        context.checking(new Expectations() {{
            oneOf(kollector).accumulator(); will(returnValue(accumulator));

            Sequence filling = context.sequence("filling");
            Sequence bread = context.sequence("bread");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate(Sandvich.filling, Cheese); inSequence(filling);
            oneOf(accumulator).accumulate(Sandvich.bread, White); inSequence(bread);
            oneOf(accumulator).accumulate(Sandvich.style, Burger); inSequence(style);
            oneOf(kollector).finish(accumulator); will(returnValue("result!")); inSequences(filling, bread, style);
        }});

        String result = sandvich.collect(kollector);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_Rekord_collector_can_take_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final Accumulator accumulator = accumulator();
        final Kollector<Accumulator, Integer> kollector = kollector();

        context.checking(new Expectations() {{
            oneOf(kollector).accumulator(); will(returnValue(accumulator));

            Sequence curvature = context.sequence("curvature");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate(Wurst.curvature, 0.7); inSequence(curvature);
            oneOf(accumulator).accumulate(Bratwurst.style, Chopped); inSequence(style);
            oneOf(kollector).finish(accumulator); will(returnValue(99)); inSequences(curvature, style);
        }});

        int result = bratwurst.collect(kollector);

        context.assertIsSatisfied();
        assertThat(result, is(99));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Rekord.of(Bier.class)
                                       .with(Bier.volume, Measurement.of(568).ml())
                                       .with(Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head=3cm"), containsString("volume=568ml"))));
    }

    @SuppressWarnings("unchecked")
    private <A extends Accumulator, R> Kollector<A, R> kollector() {
        return context.mock(Kollector.class);
    }

    private Accumulator accumulator() {
        return context.mock(Accumulator.class);
    }
}
