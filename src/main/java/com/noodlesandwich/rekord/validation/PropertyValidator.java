package com.noodlesandwich.rekord.validation;

public interface PropertyValidator<T> {
    void test(T value) throws InvalidRekordException;
}
