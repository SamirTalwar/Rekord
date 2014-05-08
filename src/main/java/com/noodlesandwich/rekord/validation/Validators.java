package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Validators {
    private Validators() { }

    public static <T> Validator<T> that(final BooleanValidator<T> validator) {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                if (!validator.test(rekord)) {
                    throw new UnspecifiedInvalidRekordException();
                }
            }
        };
    }

    public static <T> Validator<T> toAlwaysSucceed() {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) { }
        };
    }

    public static <T> Validator<T> toAlwaysFail() {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                throw new UnspecifiedInvalidRekordException();
            }
        };
    }

    public static <T> Validator<T> allProperties() {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                PSet<Key<? super T, ?>> expectedKeys = rekord.acceptedKeys();
                PSet<Key<? super T, ?>> actualKeys = rekord.keys();
                PSet<Key<? super T, ?>> missingKeys = expectedKeys.minusAll(actualKeys);
                if (!missingKeys.isEmpty()) {
                    throw new MissingPropertiesException(missingKeys);
                }
            }
        };
    }

    @SafeVarargs
    public static <T> Validator<T> theProperties(final Key<? super T, ?>... keys) {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                PSet<Key<? super T, ?>> expectedKeys = OrderedPSet.from(Arrays.asList(keys));
                PSet<Key<? super T, ?>> actualKeys = rekord.keys();
                PSet<Key<? super T, ?>> missingKeys = expectedKeys.minusAll(actualKeys);
                if (!missingKeys.isEmpty()) {
                    throw new MissingPropertiesException(missingKeys);
                }
            }
        };
    }

    public static <T, V> Validator<T> theProperty(final Key<T, V> key, final PropertyValidator<V> propertyValidator) {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                propertyValidator.test(rekord.get(key));
            }
        };
    }

    public static <T> PropertyValidator<T> isEqualTo(final T expected) {
        return new PropertyValidator<T>() {
            @Override public void test(T actual) throws InvalidRekordException {
                if (!expected.equals(actual)) {
                    throw new InvalidRekordException(String.format("Expected the value <%s>, but got <%s>.", expected, actual));
                }
            }
        };
    }

    public static <T> PropertyValidator<T> isNotEqualTo(final T expected) {
        return new PropertyValidator<T>() {
            @Override
            public void test(T actual) throws InvalidRekordException {
                if (expected.equals(actual)) {
                    throw new InvalidRekordException(String.format("Expected any value but <%s>, but got <%s>.", expected, actual));
                }
            }
        };
    }

    public static final class UnspecifiedInvalidRekordException extends InvalidRekordException { }

    public static final class MissingPropertiesException extends InvalidRekordException {
        public <T> MissingPropertiesException(PSet<Key<? super T, ?>> keys) {
            super(String.format("The rekord was missing the properties %s.", keys));
        }
    }
}
