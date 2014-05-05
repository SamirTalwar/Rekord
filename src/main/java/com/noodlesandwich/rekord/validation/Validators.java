package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;

public final class Validators {
    private Validators() { }

    public static <T> Validator<T> when(final BooleanValidator<T> validator) {
        return new Validator<T>() {
            @Override public void test(FixedRekord<T> rekord) throws InvalidRekordException {
                if (!validator.test(rekord)) {
                    throw new InvalidRekordException("The rekord was invalid.");
                }
            }
        };
    }
}
