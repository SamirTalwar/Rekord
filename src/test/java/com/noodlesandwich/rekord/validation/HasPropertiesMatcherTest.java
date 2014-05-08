package com.noodlesandwich.rekord.validation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.testobjects.ExceptionMatcher.an;
import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.validation.RekordMatchers.aRekordOf;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class HasPropertiesMatcherTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final ValidatingRekord<Address> validatingAddressRequiringSomeProperties = ValidatingRekord.validating(Address.rekord)
            .expecting(hasProperties(Address.street, Address.city));

    @Test public void
    accepts_a_rekord_with_all_specified_properties() throws InvalidRekordException {
        ValidRekord<Address> address = validatingAddressRequiringSomeProperties
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
        expectedException.expect(an(InvalidRekordException.class)
                .withTheMessage("Expected that the rekord has properties with the keys <[street, city]>, but was missing the keys <[city]>."));

        validatingAddressRequiringSomeProperties
                .with(Address.houseNumber, 22)
                .with(Address.street, "Acacia Avenue")
                .fix();
    }
}
