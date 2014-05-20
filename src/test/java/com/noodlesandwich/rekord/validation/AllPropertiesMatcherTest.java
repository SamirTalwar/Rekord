package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public final class AllPropertiesMatcherTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Address> validatingAddressRequiringAllProperties = ValidatingRekord.validating(Address.rekord)
            .expecting(RekordMatchers.<Address>allProperties());

    @Test public void
    accepts_a_rekord_with_all_its_properties_when_expecting_all_properties() throws InvalidRekordException {
        ValidRekord<Address> address = validatingAddressRequiringAllProperties
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
    rejects_a_rekord_with_a_missing_property_when_expecting_all_properties() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage(anyOf(
                        equalTo("Expected that all properties are set, but was missing the keys <[city, postal code]>."),
                        equalTo("Expected that all properties are set, but was missing the keys <[postal code, city]>."))));

        validatingAddressRequiringAllProperties
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .fix();
    }
}
