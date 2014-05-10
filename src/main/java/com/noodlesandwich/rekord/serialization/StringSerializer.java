package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public final class StringSerializer implements Serializer<String> {
    @Override
    public <T> String serialize(FixedRekord<T> rekord) {
        String rekordContents = Serialization.serialize(rekord).into(new StringAccumulator(Formatter.Rekord));
        String name = rekord.name();
        return rekordString(name, rekordContents);
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
        public void addCollection(String name, Accumulation<String> accumulation) {
            StringAccumulator collectionAccumulator = new StringAccumulator(Formatter.Collection);
            accumulation.accumulateIn(collectionAccumulator);
            builder.add(name, String.format("[%s]", collectionAccumulator.result()));
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<String> accumulation) {
            StringAccumulator rekordAccumulator = new StringAccumulator(Formatter.Rekord);
            accumulation.accumulateIn(rekordAccumulator);
            builder.add(name, rekordString(rekordName, rekordAccumulator.result()));
        }

        @Override
        public String result() {
            return builder.toString();
        }
    }

    private static enum Formatter {
        Collection {
            @Override public String format(String name, Object value) {
                return value.toString();
            }
        },
        Rekord {
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
