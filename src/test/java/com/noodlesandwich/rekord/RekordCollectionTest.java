package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.testobjects.Measurement;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static com.noodlesandwich.rekord.Kollector.Accumulator;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bier;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst;
import static com.noodlesandwich.rekord.testobjects.Rekords.Bratwurst.Style.Chopped;
import static com.noodlesandwich.rekord.testobjects.Rekords.Person;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Bread.White;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Filling.Cheese;
import static com.noodlesandwich.rekord.testobjects.Rekords.Sandvich.Style.Burger;
import static com.noodlesandwich.rekord.testobjects.Rekords.Wurst;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

public final class RekordCollectionTest {
    private final Mockery context = new Mockery();

    @Test public void
    a_Rekord_can_be_collected_into_a_collector() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.bread, White)
                .with(Sandvich.style, Burger);

        final Accumulator<String> accumulator = accumulator();
        final Kollector<Accumulator<String>, String> kollector = kollector();

        context.checking(new Expectations() {{
            oneOf(kollector).accumulatorNamed("Sandvich"); will(returnValue(accumulator));

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
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final Accumulator<Integer> accumulator = accumulator();
        final Kollector<Accumulator<Integer>, Integer> kollector = kollector();

        context.checking(new Expectations() {{
            oneOf(kollector).accumulatorNamed("Bratwurst"); will(returnValue(accumulator));

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
    a_Rekord_collector_can_nest_itself() {
        Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Sherlock")
                .with(Person.lastName, "Holmes")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 221)
                        .with(Address.street, "Baker Street"));

        final Accumulator<String> personAccumulator = accumulator("person accumulator");
        final Accumulator<String> addressAccumulator = accumulator("address accumulator");
        final Kollector<Accumulator<String>, String> kollector = kollector();

        context.checking(new Expectations() {{
            oneOf(kollector).accumulatorNamed("Person"); will(returnValue(personAccumulator));
            oneOf(personAccumulator).accumulate(Person.firstName, "Sherlock");
            oneOf(personAccumulator).accumulate(Person.lastName, "Holmes");

            oneOf(kollector).accumulatorNamed("Address"); will(returnValue(addressAccumulator));
            oneOf(addressAccumulator).accumulate(Address.houseNumber, 221);
            oneOf(addressAccumulator).accumulate(Address.street, "Baker Street");
            oneOf(kollector).finish(addressAccumulator); will(returnValue("221 Baker Street"));
            oneOf(personAccumulator).accumulateRekord(Person.address, "221 Baker Street");

            oneOf(kollector).finish(personAccumulator); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = person.collect(kollector);

        context.assertIsSatisfied();
        assertThat(result, is("Sherlock Holmes, 221 Baker Street"));
    }

    @Test public void
    a_Rekord_is_serializable_as_a_String() {
        Rekord<Bier> delicious = Bier.rekord
                .with(Bier.volume, Measurement.of(568).ml())
                .with(Bier.head, Measurement.of(3).cm());

        assertThat(delicious, hasToString(allOf(startsWith("Bier"), containsString("head: 3cm"), containsString("volume: 568ml"))));
    }

    @SuppressWarnings("unchecked")
    private <A extends Accumulator<R>, R> Kollector<A, R> kollector() {
        return context.mock(Kollector.class);
    }

    @SuppressWarnings("unchecked")
    private <R> Accumulator<R> accumulator() {
        return context.mock(Accumulator.class);
    }

    @SuppressWarnings("unchecked")
    private <R> Accumulator<R> accumulator(String name) {
        return context.mock(Accumulator.class, name);
    }
}
