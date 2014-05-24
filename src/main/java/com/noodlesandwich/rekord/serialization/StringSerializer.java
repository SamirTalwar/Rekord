package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;

public final class StringSerializer implements SafeSerializer<String> {
    @Override
    public <T> String serialize(Key<?, FixedRekord<T>> key, FixedRekord<T> rekord) {
        StringAccumulator accumulator = new StringAccumulator(Formatter.Value);
        key.accumulate(rekord, accumulator);
        return accumulator.result();
    }

    private static String rekordString(String name, String contents) {
        return String.format("%s {%s}", name, contents);
    }

    private static final class StringAccumulator implements SafeAccumulator<String> {
        private final EntryStringBuilder builder;

        public StringAccumulator(Formatter formatter) {
            this.builder = new EntryStringBuilder(formatter);
        }

        @Override
        public void addValue(String name, Object value) {
            builder.add(name, value);
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) {
            StringAccumulator iterableAccumulator = new StringAccumulator(Formatter.Value);
            accumulation.accumulateIn(iterableAccumulator);
            builder.add(name, String.format("[%s]", iterableAccumulator.result()));
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) {
            StringAccumulator rekordAccumulator = new StringAccumulator(Formatter.Entry);
            accumulation.accumulateIn(rekordAccumulator);
            builder.add(name, rekordString(rekordName, rekordAccumulator.result()));
        }

        @Override
        public String result() {
            return builder.toString();
        }
    }

    private static enum Formatter {
        Value {
            @Override public String format(String name, Object value) {
                return value.toString();
            }
        },
        Entry {
            @Override public String format(String name, Object value) {
                return String.format("%s: %s", name, value.toString());
            }
        };

        public abstract String format(String name, Object value);
    }

    private static final class EntryStringBuilder {
        private final Formatter formatter;
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        public EntryStringBuilder(Formatter formatter) {
            this.formatter = formatter;
        }

        public void add(String name, Object value) {
            appendSeparator();
            entries.append(formatter.format(name, value));
        }

        @Override
        public String toString() {
            return entries.toString();
        }

        private void appendSeparator() {
            if (!first) {
                entries.append(", ");
            } else {
                first = false;
            }
        }
    }
}
