package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;

public interface Validator<T> {
    void test(FixedRekord<T> rekord) throws InvalidRekordException;
}
