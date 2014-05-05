package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;

public final class Validators {
    private Validators() { }

    public static <T> Validator<T> when(final BooleanValidator<T> validator) {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                if (!validator.test(rekord)) {
                    throw new UnknownRekordValidationException();
                }
            }
        };
    }

    public static <T> Validator<T> everything() {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) { }
        };
    }

    public static <T> Validator<T> nothing() {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                throw new UnknownRekordValidationException();
            }
        };
    }

    public static final class UnknownRekordValidationException extends InvalidRekordException {
        public UnknownRekordValidationException() {
            super("The rekord was invalid.");
        }
    }
}
