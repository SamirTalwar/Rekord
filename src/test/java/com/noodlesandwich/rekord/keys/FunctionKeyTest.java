package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class FunctionKeyTest {
    @Test public void
    modifies_an_existing_key() {
        Key<Address, String> houseNumberString = FunctionKey.named("house number as string").wrapping(Address.houseNumber).with(IntegerToString);

        Rekord<Address> address = Address.rekord
                .with(houseNumberString, "42")
                .with(Address.street, "Maida Vale");

        assertThat(address.get(houseNumberString), is("42"));
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
}
