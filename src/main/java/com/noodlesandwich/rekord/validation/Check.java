package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.Rekord;

public interface Check<T> {
    boolean check(Rekord<T> rekord);
}
