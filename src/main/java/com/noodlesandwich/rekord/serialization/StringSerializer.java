package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Key;

public final class StringSerializer implements Serializer<String, String> {
    @Override
    public Accumulator<String, String> accumulatorNamed(String name) {
        return new StringAccumulator(name);
    }

    public static final class StringAccumulator implements Serializer.Accumulator<String, String> {
        private final String name;
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        public StringAccumulator(String name) {
            this.name = name;
        }

        @Override
        public <V> void accumulate(Key<?, V> key, V value) {
            append(key, value);
        }

        @Override
        public void accumulateNested(Key<?, ?> key, Accumulator<String, String> nested) {
            append(key, nested.value());
        }

        @Override
        public Accumulator<String, String> nest(String name) {
            return new StringAccumulator(name);
        }

        @Override
        public String value() {
            return String.format("%s {%s}", name, entries);
        }

        @Override
        public String finish() {
            return value();
        }

        private void append(Key<?, ?> key, Object value) {
            appendSeparator();
            entries.append(String.format("%s: %s", key.name(), value));
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
