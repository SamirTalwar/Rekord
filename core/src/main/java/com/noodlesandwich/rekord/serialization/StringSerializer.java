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

    private static final class StringAccumulation<T> implements SafeAccumulation {
        private final FixedRekord<T> rekord;

        public StringAccumulation(FixedRekord<T> rekord) {
            this.rekord = rekord;
        }

        @Override
        public <A2> void accumulateIn(Accumulator<A2, ImpossibleException> accumulator) {
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
        public void addCollection(String name, Serializer.Accumulation<ImpossibleException> accumulation) {
            StringAccumulator accumulator = new StringAccumulator(Formatter.Value);
            accumulation.accumulateIn(accumulator);
            builder.addCollection(name, accumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Serializer.Accumulation<ImpossibleException> accumulation) {
            StringAccumulator accumulator = new StringAccumulator(Formatter.Entry);
            accumulation.accumulateIn(accumulator);
            builder.addRekord(name, rekordName, accumulator.result());
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

        public void addCollection(String name, String collection) {
            add(name, String.format(CollectionFormat, collection));
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
