package com.noodlesandwich.rekord.keys;

import java.util.regex.Pattern;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.implementation.PersistentPropertyMap;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    private static InvertibleFunction<PropertyMap<Address>, String> concatenateHouseNumberAndStreet() {
        return new InvertibleFunction<PropertyMap<Address>, String>() {
            private final Pattern whitespace = Pattern.compile(" ");

            @Override
            public String applyForward(PropertyMap<Address> input) {
                return String.format("%d %s", input.get(Address.houseNumber), input.get(Address.street));
            }

            @Override
            public PropertyMap<Address> applyBackward(String input) {
                String[] split = whitespace.split(input, 2);
                return new PersistentPropertyMap<Address>()
                        .set(new Property<>(Address.houseNumber, Integer.parseInt(split[0])))
                        .set(new Property<>(Address.street, split[1]));
            }
        };
    }
}
