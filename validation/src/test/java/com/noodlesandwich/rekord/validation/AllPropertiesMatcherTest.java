package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.test.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Country.UnitedKingdom;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

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
                .with(Address.country, UnitedKingdom)
                .fix();

        assertThat(address, is(aRekordOf(Address.class)
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .with(Address.city, "Aberdeen")
                .with(Address.postalCode, "AB1 2CD")
                .with(Address.country, UnitedKingdom)));
    }

    @Test public void
    rejects_a_rekord_with_a_missing_property_when_expecting_all_properties() throws InvalidRekordException {
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage(allOf(
                        startsWith("Expected that all properties are set, but was missing the keys <["),
                        containsString("city"),
                        containsString("postal code"),
                        containsString("country"),
                        endsWith("]>."))));

        validatingAddressRequiringAllProperties
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .fix();
    }
}
