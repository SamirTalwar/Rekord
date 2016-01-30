package com.noodlesandwich.rekord.keys;

import java.util.regex.Pattern;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.implementation.PersistentProperties;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public final class OneToManyKeyTest {
    private static final Key<Address, String> firstLine =
            OneToManyKey
                .named("first line")
                .over(Address.houseNumber, Address.street)
                .with(concatenateHouseNumberAndStreet());

    @Test public void
    maps_a_single_key_onto_many_keys_during_storage() {
        Rekord<Address> address = Address.rekord
                .with(firstLine, "1 Infinite Loop")
                .with(Address.city, "Cupertino");

        assertThat(address, is(Address.rekord
                .with(Address.houseNumber, 1)
                .with(Address.street, "Infinite Loop")
                .with(Address.city, "Cupertino")));
    }

    @Test public void
    maps_many_keys_onto_a_single_key_during_retrieval() {
        Rekord<Address> address = Address.rekord
                .with(Address.houseNumber, 37)
                .with(Address.street, "Duckbill Street")
                .with(Address.city, "Paloma");

        assertThat(address.get(firstLine), is("37 Duckbill Street"));
    }

    @Test public void
    tests_as_true_when_all_keys_are_present() {
        Rekord<Address> address = Address.rekord
                .with(Address.houseNumber, 99)
                .with(Address.street, "Luftballons")
                .with(Address.city, "Berlin");

        assertThat(address, hasKey(firstLine));
    }

    @Test public void
    tests_as_false_when_any_key_is_absent() {
        Rekord<Address> address = Address.rekord
                .with(Address.street, "Nope")
                .with(Address.city, "Berlin");

        assertThat(address, not(hasKey(firstLine)));
    }

    private static InvertibleFunction<Properties<Address>, String> concatenateHouseNumberAndStreet() {
        return new InvertibleFunction<Properties<Address>, String>() {
            private final Pattern whitespace = Pattern.compile(" ");

            @Override
            public String applyForward(Properties<Address> input) {
                return String.format("%d %s", input.get(Address.houseNumber), input.get(Address.street));
            }

            @Override
            public Properties<Address> applyBackward(String input) {
                String[] split = whitespace.split(input, 2);
                return new PersistentProperties<Address>()
                        .set(new Property<>(Address.houseNumber, Integer.parseInt(split[0])))
                        .set(new Property<>(Address.street, split[1]));
            }
        };
    }
}
