package com.noodlesandwich.rekord;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.Rekords.Sandvich.Style.Burger;

public final class RekordCollectionTest {
    private final Mockery context = new Mockery();

    @Test public void
    a_Rekord_can_be_collected_into_a_collector() {
        Rekord<Rekords.Sandvich> sandvich = Rekord.of(Rekords.Sandvich.class)
                .with(Rekords.Sandvich.filling, Cheese)
                .with(Rekords.Sandvich.bread, White)
                .with(Rekords.Sandvich.style, Burger);

        @SuppressWarnings("unchecked")
        final RekordCollector<Rekords.Sandvich, String> collector = context.mock(RekordCollector.class);
        final String expectedResult = "result!";
        context.checking(new Expectations() {{
            Sequence filling = context.sequence("filling");
            Sequence bread = context.sequence("bread");
            Sequence style = context.sequence("style");
            oneOf(collector).accumulate(Rekords.Sandvich.filling, Cheese); inSequence(filling);
            oneOf(collector).accumulate(Rekords.Sandvich.bread, White); inSequence(bread);
            oneOf(collector).accumulate(Rekords.Sandvich.style, Burger); inSequence(style);
            oneOf(collector).result(); will(returnValue(expectedResult)); inSequences(filling, bread, style);
        }});

        String actualResult = sandvich.collect(collector);

        context.assertIsSatisfied();
        assertThat(actualResult, is(expectedResult));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Rekords.Bier> delicious = Rekord.of(Rekords.Bier.class)
                                       .with(Rekords.Bier.volume, Measurement.of(568).ml())
                                       .with(Rekords.Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head=3cm"), containsString("volume=568ml"))));
    }
}
