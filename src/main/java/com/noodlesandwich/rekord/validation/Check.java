package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;

public interface Check<T> {
    boolean check(FixedRekord<T> rekord);
}
