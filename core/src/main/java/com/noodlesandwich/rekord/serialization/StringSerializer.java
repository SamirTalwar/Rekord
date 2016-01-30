package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public final class StringSerializer implements SafeSerializer<String> {
    private static final String EntryFormat = "%s: %s";
    private static final String CollectionFormat = "[%s]";
    private static final String RekordFormat = "%s {%s}";

    @Override
    public <T> String serialize(String name, final FixedRekord<T> rekord) {
        StringAccumulator accumulator = new StringAccumulator(Formatter.Value);
        accumulator.addRekord(name, rekord.name(), new StringAccumulation<>(rekord));
        return accumulator.result();
    }

    private static final class StringAccumulation<T> implements Accumulation {
        private final FixedRekord<T> rekord;

        public StringAccumulation(FixedRekord<T> rekord) {
            this.rekord = rekord;
        }

        @Override
        public <A2, E2 extends Exception> void accumulateIn(Accumulator<A2, E2> accumulator) throws E2 {
            Serialization.serialize(rekord).into(accumulator);
        }
    }

    private static final class StringAccumulator implements SafeAccumulator<String> {
        private final CollectionStringBuilder builder;

        public StringAccumulator(Formatter formatter) {
            this.builder = new CollectionStringBuilder(formatter);
        }

        @Override
        public void addValue(String name, Object value) {
            builder.add(name, value);
        }

        @Override
        public void addIterable(String name, Serializer.Accumulation accumulation) {
            StringAccumulator iterableAccumulator = new StringAccumulator(Formatter.Value);
            accumulation.accumulateIn(iterableAccumulator);
            builder.addIterable(name, iterableAccumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Serializer.Accumulation accumulation) {
            StringAccumulator rekordAccumulator = new StringAccumulator(Formatter.Entry);
            accumulation.accumulateIn(rekordAccumulator);
            builder.addRekord(name, rekordName, rekordAccumulator.result());
        }

        @Override
        public String result() {
            return builder.toString();
        }
    }

    private enum Formatter {
        Value {
            @Override public String format(String name, Object value) {
                return value.toString();
            }
        },
        Entry {
            @Override public String format(String name, Object value) {
                return String.format(EntryFormat, name, value.toString());
            }
        };

        public abstract String format(String name, Object value);
    }

    private static final class CollectionStringBuilder {
        private final Formatter formatter;
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        public CollectionStringBuilder(Formatter formatter) {
            this.formatter = formatter;
        }

        public void addIterable(String name, String iterable) {
            add(name, String.format(CollectionFormat, iterable));
        }

        public void addRekord(String name, String rekordName, String rekord) {
            add(name, String.format(RekordFormat, rekordName, rekord));
        }

        @Override
        public String toString() {
            return entries.toString();
        }

        private void add(String name, Object value) {
            appendSeparator();
            entries.append(formatter.format(name, value));
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
