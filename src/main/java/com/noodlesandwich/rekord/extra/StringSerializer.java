package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Serializer;

public final class StringSerializer implements Serializer<String> {
    @Override
    public Accumulator<String> accumulatorNamed(String name) {
        return new StringAccumulator(name);
    }

    public static final class StringAccumulator implements Serializer.Accumulator<String> {
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
        public void accumulateRekord(Key<?, ?> key, String serializedRekord) {
            append(key, serializedRekord);
        }

        @Override
        public String finish() {
            return String.format("%s {%s}", name, entries);
        }

        private void append(Key<?, ?> key, Object value) {
            appendSeparator();
            entries.append(String.format("%s: %s", key, value));
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
