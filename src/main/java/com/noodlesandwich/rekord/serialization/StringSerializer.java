package com.noodlesandwich.rekord.serialization;

public final class StringSerializer implements Serializer<String, String> {
    @Override
    public Accumulator<String> start(String name) {
        return new StringAccumulator(name);
    }

    @Override
    public String finish(Accumulator<String> accumulator) {
        return accumulator.value();
    }

    public static final class StringAccumulator implements Serializer.Accumulator<String> {
        private final String name;
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        public StringAccumulator(String name) {
            this.name = name;
        }

        @Override
        public void accumulate(String name, Object value) {
            append(name, value);
        }

        @Override
        public void accumulateNested(String name, Accumulator<String> accumulator) {
            append(name, accumulator.value());
        }

        @Override
        public Accumulator<String> nest(String name) {
            return new StringAccumulator(name);
        }

        @Override
        public String value() {
            return String.format("%s {%s}", name, entries);
        }

        private void append(String name, Object value) {
            appendSeparator();
            entries.append(String.format("%s: %s", name, value));
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
