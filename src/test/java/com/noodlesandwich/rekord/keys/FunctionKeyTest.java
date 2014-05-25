package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class FunctionKeyTest {
    @Test public void
    modifies_an_existing_key() {
        Rekord<Address> address = Address.rekord
                .with(houseNumberString, "42")
                .with(Address.street, "Maida Vale");

        assertThat(address.get(houseNumberString), is("42"));
    }

    @Test public void
    returns_null_if_there_is_no_value_present() {
        Rekord<Address> address = Address.rekord
                .with(Address.street, "Tottenham Court Road");

        assertThat(address.get(houseNumberString), is(nullValue()));
    }

    private static final InvertibleFunction<Integer, String> IntegerToString = new InvertibleFunction<Integer, String>() {
        @Override
        public String applyForward(Integer input) {
            return input.toString();
        }

        @Override
        public Integer applyBackward(String input) {
            return Integer.parseInt(input);
        }
    };

    private static final Key<Address, String> houseNumberString = FunctionKey.named("house number as string").wrapping(Address.houseNumber).with(IntegerToString);
}
