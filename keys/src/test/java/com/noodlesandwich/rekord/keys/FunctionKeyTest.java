package com.noodlesandwich.rekord.keys;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.noodlesandwich.rekord.functions.Functions.invertibleIdentity;
import static com.noodlesandwich.rekord.test.ExceptionMatcher.a;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Address;
import static com.noodlesandwich.rekord.testobjects.TestRekords.Country;
import static com.noodlesandwich.rekord.validation.RekordMatchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public final class FunctionKeyTest {
    @Rule public final ExpectedException expectedException = ExpectedException.none();

    private static final Key<Address, String> houseNumberString
            = FunctionKey.wrapping(Address.houseNumber).with(integerToString());

    private static final Key<Address, Integer> superstitiousHouseNumber
            = FunctionKey.wrapping(Address.houseNumber).with(superstition());

    private static final Key<Address, CountryCode> countryCode
            = FunctionKey.named("country code").wrapping(Address.country).with(countryCodeMapping());

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

    @Test public void
    tests_as_present_when_a_value_has_been_previously_stored() {
        Rekord<Address> address = Address.rekord
                .with(countryCode, CountryCode.AU);

        assertThat(address, hasKey(countryCode));
    }

    @Test public void
    tests_as_absent_when_a_value_has_not_been_previously_stored() {
        Rekord<Address> address = Address.rekord
                .with(Address.street, "Wallaby Way");

        assertThat(address, not(hasKey(countryCode)));
    }

    @Test public void
    tests_as_absent_if_the_returned_value_from_the_function_is_null() {
        Rekord<Address> address = Address.rekord
                .with(Address.houseNumber, 13);

        assertThat(address, not(hasKey(superstitiousHouseNumber)));
    }

    @Test public void
    the_underlying_key_can_be_used_for_retrieval() {
        Rekord<Address> address = Address.rekord
                .with(houseNumberString, "42");

        assertThat(address.get(Address.houseNumber), is(42));
    }

    @Test public void
    the_underlying_key_can_be_used_for_storage() {
        Rekord<Address> address = Address.rekord
                .with(Address.houseNumber, 42);

        assertThat(address.get(houseNumberString), is("42"));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    provides_the_underlying_key_when_iterating() {
        assertThat(houseNumberString, Matchers.<Key<Address, ?>>contains(Address.houseNumber));
    }

    @Test public void
    uses_the_original_rekord_name_if_none_is_provided() {
        assertThat(houseNumberString.name(), is("house number"));
    }

    @Test public void
    rejects_a_null_name() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("The name of a key must not be null."));

        FunctionKey.named(null).wrapping(SimpleKey.named("name")).with(invertibleIdentity());
    }

    @Test public void
    rejects_a_null_key() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("The underlying key of a FunctionKey must not be null."));

        FunctionKey.named("name").wrapping(null).with(invertibleIdentity());
    }

    @Test public void
    rejects_a_null_function() {
        expectedException.expect(a(NullPointerException.class)
                .withTheMessage("The function of a FunctionKey must not be null."));

        FunctionKey.named("name").wrapping(SimpleKey.named("name")).with(null);
    }

    private static InvertibleFunction<Integer, String> integerToString() {
        return new InvertibleFunction<Integer, String>() {
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

    private static InvertibleFunction<Integer, Integer> superstition() {
        return new InvertibleFunction<Integer, Integer>() {
            private final Set<Integer> unlucky = ImmutableSet.of(4, 7, 13);

            @Override
            public Integer applyForward(Integer input) {
                if (unlucky.contains(input)) {
                    return null;
                }
                return input;
            }

            @Override
            public Integer applyBackward(Integer input) {
                if (unlucky.contains(input)) {
                    return null;
                }
                return input;
            }
        };
    }

    private static InvertibleFunction<Country, CountryCode> countryCodeMapping() {
        return new InvertibleFunction<Country, CountryCode>() {
            @Override
            public CountryCode applyForward(Country input) {
                return CountryCode.ForwardMapping.get(input);
            }

            @Override
            public Country applyBackward(CountryCode input) {
                return CountryCode.BackwardMapping.get(input);
            }
        };
    }

    @SuppressWarnings("unused")
    private enum CountryCode {
        AU(Country.Australia),
        AT(Country.Austria),
        CA(Country.Canada),
        DE(Country.Germany),
        NZ(Country.NewZealand),
        CH(Country.Switzerland),
        GB(Country.UnitedKingdom),
        US(Country.UnitedStates);

        private final Country country;

        CountryCode(Country country) {
            this.country = country;
        }

        private static final BiMap<Country, CountryCode> Mapping = Maps.unmodifiableBiMap(HashBiMap.create(Maps.uniqueIndex(Arrays.asList(values()), new Function<CountryCode, Country>() {
            @Override
            public Country apply(CountryCode countryCode) {
                return countryCode.country;
            }
        })));

        private static final Map<Country, CountryCode> ForwardMapping = Mapping;

        private static final Map<CountryCode, Country> BackwardMapping = Mapping.inverse();
    }
}
