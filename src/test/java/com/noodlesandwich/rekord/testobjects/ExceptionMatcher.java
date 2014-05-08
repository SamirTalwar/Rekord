package com.noodlesandwich.rekord.testobjects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.Matchers.instanceOf;

public final class ExceptionMatcher<T extends Throwable> extends TypeSafeDiagnosingMatcher<T> {
    private final Class<T> expectedExceptionClass;
    private final String expectedMessage;

    public ExceptionMatcher(Class<T> expectedExceptionClass, String expectedMessage) {
        this.expectedExceptionClass = expectedExceptionClass;
        this.expectedMessage = expectedMessage;
    }

    public static <T extends Throwable> ExceptionMatcherBuilder<T> a(Class<T> exceptionClass) {
        return new ExceptionMatcherBuilder<>(exceptionClass);
    }

    public static <T extends Throwable> ExceptionMatcherBuilder<T> an(Class<T> exceptionClass) {
        return a(exceptionClass);
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("a ")
                .appendValue(expectedExceptionClass)
                .appendText(" with the message ")
                .appendValue(expectedMessage);
    }

    @Override
    protected boolean matchesSafely(T actualException, Description mismatchDescription) {
        Class<?> actualExceptionClass = actualException.getClass();
        String actualMessage = actualException.getMessage();

        mismatchDescription
                .appendText("was a ")
                .appendValue(actualExceptionClass)
                .appendText(" with the message ")
                .appendValue(actualMessage);

        return instanceOf(expectedExceptionClass).matches(actualException)
                && expectedMessage.equals(actualMessage);
    }

    public static class ExceptionMatcherBuilder<T extends Throwable> {
        private final Class<T> exceptionClass;

        public ExceptionMatcherBuilder(Class<T> exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public Matcher<T> withTheMessage(String message) {
            return new ExceptionMatcher<>(exceptionClass, message);
        }
    }
}
