package com.noodlesandwich.rekord.keys;

import java.util.Arrays;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.functions.InvertibleFunction;
import org.junit.Test;

import static com.noodlesandwich.rekord.testobjects.Rekords.Address;
import static com.noodlesandwich.rekord.testobjects.Rekords.Country;
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

    @Test public void
    tests_as_present_when_a_value_has_been_previously_stored() {
        Rekord<Address> address = Address.rekord
                .with(countryCode, CountryCode.AU);

        assertThat(address.has(countryCode), is(true));
    }

    @Test public void
    tests_as_absent_when_a_value_has_not_been_previously_stored() {
        Rekord<Address> address = Address.rekord
                .with(Address.street, "Wallaby Way");

        assertThat(address.has(countryCode), is(false));
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

    private static final InvertibleFunction<Country, CountryCode> CountryCodeMapping = new InvertibleFunction<Country, CountryCode>() {
        @Override
        public CountryCode applyForward(Country input) {
            return CountryCode.ForwardMapping.get(input);
        }

        @Override
        public Country applyBackward(CountryCode input) {
            return CountryCode.BackwardMapping.get(input);
        }
    };

    private static enum CountryCode {
        AU(Country.Australia),
        AT(Country.Austria),
        CA(Country.Canada),
        DE(Country.Germany),
        NZ(Country.NewZealand),
        CH(Country.Switzerland),
        GB(Country.UnitedKingdom),
        US(Country.UnitedStates);

        private final Country country;

        private CountryCode(Country country) {
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

    private static final Key<Address, String> houseNumberString = FunctionKey.named("house number as string").wrapping(Address.houseNumber).with(IntegerToString);
    private static final Key<Address, CountryCode> countryCode = FunctionKey.named("country code").wrapping(Address.country).with(CountryCodeMapping);
}
