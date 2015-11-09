package com.noodlesandwich.rekord.views;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import com.noodlesandwich.rekord.keys.CopiedKey;
import com.noodlesandwich.rekord.keys.FunctionKey;
import com.noodlesandwich.rekord.keys.Key;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class RekordTransformationTest {
    public interface StringedAddress {
        Key<StringedAddress, String> houseNumber = CopiedKey.from(FunctionKey.wrapping(Address.houseNumber).with(IntegerToString));
        Key<StringedAddress, String> street = CopiedKey.from(Address.street);

        RekordView<StringedAddress, Address> view = RekordView.of(StringedAddress.class)
                .viewing(Address.rekord)
                .accepting(houseNumber, street);
    }

    @Test public void
    a_Rekord_can_be_viewed_differently() {
        Rekord<Address> address = Address.rekord
                .with(Address.houseNumber, 21)
                .with(Address.street, "Jump Street");

        FixedRekord<StringedAddress> transformedAddress = StringedAddress.view.over(address);

        assertThat(transformedAddress, is(aRekordOf(StringedAddress.class)
                .with(StringedAddress.houseNumber, "21")
                .with(StringedAddress.street, "Jump Street")));
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
