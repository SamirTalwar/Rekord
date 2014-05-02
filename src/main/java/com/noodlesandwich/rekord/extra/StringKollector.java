package com.noodlesandwich.rekord.extra;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Kollector;

public final class StringKollector implements Kollector<StringKollector.StringAccumulator, String> {
    @Override
    public StringAccumulator accumulatorNamed(String name) {
        return new StringAccumulator(name);
    }

    @Override
    public String finish(StringAccumulator accumulator) {
        return accumulator.result();
    }

    public static final class StringAccumulator implements Kollector.Accumulator<String> {
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
        public void accumulateRekord(Key<?, ?> key, String rekord) {
            append(key, rekord);
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

        public String result() {
            return String.format("%s {%s}", name, entries);
        }
    }
}
