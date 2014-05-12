package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public final class StringSerializer implements Serializer<String> {
    @Override
    public <T> String serialize(FixedRekord<T> rekord) {
        return Serialization.serialize(rekord.name(), rekord).into(new StringAccumulator(Formatter.Value));
    }

    private static String rekordString(String name, String contents) {
        return String.format("%s {%s}", name, contents);
    }

    private static final class StringAccumulator implements Accumulator<String> {
        private final EntryStringBuilder builder;

        public StringAccumulator(Formatter formatter) {
            this.builder = new EntryStringBuilder(formatter);
        }

        @Override
        public <V> void addValue(String name, V value) {
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
