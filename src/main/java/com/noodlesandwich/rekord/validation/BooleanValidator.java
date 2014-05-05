package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;

public interface BooleanValidator<T> {
    boolean test(FixedRekord<T> rekord);
}
