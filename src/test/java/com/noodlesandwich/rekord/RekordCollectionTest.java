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

        @SuppressWarnings("unchecked")
        final RekordCollector<Sandvich, String> collector = context.mock(RekordCollector.class);
        final String expectedResult = "result!";
        context.checking(new Expectations() {{
            Sequence filling = context.sequence("filling");
            Sequence bread = context.sequence("bread");
            Sequence style = context.sequence("style");
            oneOf(collector).accumulate(Sandvich.filling, Cheese); inSequence(filling);
            oneOf(collector).accumulate(Sandvich.bread, White); inSequence(bread);
            oneOf(collector).accumulate(Sandvich.style, Burger); inSequence(style);
            oneOf(collector).result(); will(returnValue(expectedResult)); inSequences(filling, bread, style);
        }});

        String actualResult = sandvich.collect(collector);

        context.assertIsSatisfied();
        assertThat(actualResult, is(expectedResult));
    }

    @Test public void
    a_Rekord_collector_can_take_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Rekord.of(Bratwurst.class)
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        @SuppressWarnings("unchecked")
        final RekordCollector<Bratwurst, Integer> collector = context.mock(RekordCollector.class);
        final int expectedResult = 99;
        context.checking(new Expectations() {{
            Sequence curvature = context.sequence("curvature");
            Sequence style = context.sequence("style");
            oneOf(collector).accumulate(Wurst.curvature, 0.7); inSequence(curvature);
            oneOf(collector).accumulate(Bratwurst.style, Chopped); inSequence(style);
            oneOf(collector).result(); will(returnValue(expectedResult)); inSequences(curvature, style);
        }});

        int actualResult = bratwurst.collect(collector);

        context.assertIsSatisfied();
        assertThat(actualResult, is(expectedResult));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Rekord.of(Bier.class)
                                       .with(Bier.volume, Measurement.of(568).ml())
                                       .with(Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head=3cm"), containsString("volume=568ml"))));
    }
}
