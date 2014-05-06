package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.matchers.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.validation.Validators.rekordsWithProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public final class SomePropertiesValidatorTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Address> validatingAddressRequiringSomeProperties = ValidRekord.validating(Address.rekord)
            .allowing(rekordsWithProperties(Address.street, Address.city));

    @Test public void
    accepts_a_rekord_with_all_specified_properties() throws InvalidRekordException {
        FixedRekord<Address> address = validatingAddressRequiringSomeProperties
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .with(Address.city, "Aberdeen")
                .with(Address.postalCode, "AB1 2CD")
                .fix();

        assertThat(address, is(aRekordOf(Address.class)
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .with(Address.city, "Aberdeen")
                .with(Address.postalCode, "AB1 2CD")));
    }

    @Test public void
    rejects_a_rekord_that_is_missing_a_specified_property() throws InvalidRekordException {
        expectedException.expect(allOf(
                instanceOf(InvalidRekordException.class),
                hasProperty("message", equalTo("The rekord was missing the properties [city]."))));

        validatingAddressRequiringSomeProperties
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .fix();
    }
}
