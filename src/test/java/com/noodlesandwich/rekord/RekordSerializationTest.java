package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.testobjects.Measurement;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.junit.Test;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
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

public final class RekordSerializationTest {
    private final Mockery context = new Mockery();

    @Test public void
    a_Rekord_can_be_serialized() {
        Rekord<Bier> bier = Bier.rekord
                .with(Bier.volume, Measurement.of(500).ml())
                .with(Bier.head, Measurement.of(1).cm());

        final Accumulator<String, String> accumulator = accumulator();
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).accumulatorNamed("Bier"); will(returnValue(accumulator));

            Sequence volume = context.sequence("volume");
            Sequence head = context.sequence("head");
            oneOf(accumulator).accumulate(Bier.volume, Measurement.of(500).ml()); inSequence(volume);
            oneOf(accumulator).accumulate(Bier.head, Measurement.of(1).cm()); inSequence(head);
            oneOf(accumulator).finish(); will(returnValue("result!")); inSequences(volume, head);
        }});

        String result = bier.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    serializes_transformed_keys() {
        Rekord<Sandvich> sandvich = Sandvich.rekord
                .with(Sandvich.bread, White)
                .with(Sandvich.filling, Cheese)
                .with(Sandvich.style, Burger);

        final Accumulator<String, String> accumulator = accumulator();
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).accumulatorNamed("Sandvich"); will(returnValue(accumulator));

            Sequence bread = context.sequence("bread");
            Sequence filling = context.sequence("filling");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate(Sandvich.bread, White); inSequence(bread);
            oneOf(accumulator).accumulate(Sandvich.filling, Cheese); inSequence(filling);
            oneOf(accumulator).accumulate(Sandvich.style, Burger); inSequence(bread);
            oneOf(accumulator).finish(); will(returnValue("result!")); inSequences(filling, bread, style);
        }});

        String result = sandvich.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is("result!"));
    }

    @Test public void
    a_serializer_will_accept_keys_of_the_supertype() {
        Rekord<Bratwurst> bratwurst = Bratwurst.rekord
                .with(Wurst.curvature, 0.7)
                .with(Bratwurst.style, Chopped);

        final Accumulator<Integer, Integer> accumulator = accumulator();
        final Serializer<Integer, Integer> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).accumulatorNamed("Bratwurst"); will(returnValue(accumulator));

            Sequence curvature = context.sequence("curvature");
            Sequence style = context.sequence("style");
            oneOf(accumulator).accumulate(Wurst.curvature, 0.7); inSequence(curvature);
            oneOf(accumulator).accumulate(Bratwurst.style, Chopped); inSequence(style);
            oneOf(accumulator).finish(); will(returnValue(99)); inSequences(curvature, style);
        }});

        int result = bratwurst.serialize(serializer);

        context.assertIsSatisfied();
        assertThat(result, is(99));
    }

    @Test public void
    serializes_nested_rekords() {
        final Rekord<Person> person = Person.rekord
                .with(Person.firstName, "Sherlock")
                .with(Person.lastName, "Holmes")
                .with(Person.address, Address.rekord
                        .with(Address.houseNumber, 221)
                        .with(Address.street, "Baker Street"));

        final Accumulator<String, String> personAccumulator = accumulator("person accumulator");
        final Accumulator<String, String> addressAccumulator = accumulator("address accumulator");
        final Serializer<String, String> serializer = serializer();

        context.checking(new Expectations() {{
            oneOf(serializer).accumulatorNamed("Person"); will(returnValue(personAccumulator));
            oneOf(personAccumulator).accumulate(Person.firstName, "Sherlock");
            oneOf(personAccumulator).accumulate(Person.lastName, "Holmes");

            oneOf(personAccumulator).nest("Address"); will(returnValue(addressAccumulator));
            oneOf(addressAccumulator).accumulate(Address.houseNumber, 221);
            oneOf(addressAccumulator).accumulate(Address.street, "Baker Street");
            oneOf(personAccumulator).accumulateNested(Person.address, addressAccumulator);

            oneOf(personAccumulator).finish(); will(returnValue("Sherlock Holmes, 221 Baker Street"));
        }});

        String result = person.serialize(serializer);

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
    private <A, R> Serializer<A, R> serializer() {
        return context.mock(Serializer.class);
    }

    @SuppressWarnings("unchecked")
    private <A, R> Accumulator<A, R> accumulator() {
        return context.mock(Accumulator.class);
    }

    @SuppressWarnings("unchecked")
    private <A, R> Accumulator<A, R> accumulator(String name) {
        return context.mock(Accumulator.class, name);
    }
}
