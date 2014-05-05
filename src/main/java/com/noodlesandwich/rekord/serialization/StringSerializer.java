package com.noodlesandwich.rekord.serialization;

public final class StringSerializer implements Serializer<String, String> {
    @Override
    public Accumulator<String> start(String name) {
        return new StringAccumulator(name);
    }

    @Override
    public String finish(Accumulator<String> accumulator) {
        return accumulator.serialized();
    }

    private static final class StringAccumulator implements Serializer.Accumulator<String> {
        private final String name;
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        public StringAccumulator(String name) {
            this.name = name;
        }

        @Override
        public SerializedProperty<String> single(String name, Object value) {
            return new SingleStringProperty(value.toString());
        }

        @Override
        public Accumulator<String> collection(String name) {
            return new StringCollectionAccumulator();
        }

        @Override
        public Accumulator<String> nest(String name) {
            return new StringAccumulator(name);
        }

        @Override
        public void accumulate(String name, SerializedProperty<String> property) {
            appendSeparator();
            entries.append(name)
                   .append(": ")
                   .append(property.serialized());
        }

        @Override
        public String serialized() {
            return String.format("%s {%s}", name, entries.toString());
        }

        private void appendSeparator() {
            if (!first) {
                entries.append(", ");
            } else {
                first = false;
            }
        }
    }

    private static final class SingleStringProperty implements SerializedProperty<String> {
        private final String value;

        public SingleStringProperty(String value) {
            this.value = value;
        }

        @Override
        public String serialized() {
            return value;
        }
    }

    private static final class StringCollectionAccumulator implements Accumulator<String> {
        private final StringBuilder entries = new StringBuilder();
        private boolean first = true;

        @Override
        public SerializedProperty<String> single(String name, Object value) {
            return new SingleStringProperty(value.toString());
        }

        @Override
        public Accumulator<String> collection(String name) {
            return new StringCollectionAccumulator();
        }

        @Override
        public Accumulator<String> nest(String name) {
            return new StringAccumulator(name);
        }

        @Override
        public void accumulate(String name, SerializedProperty<String> property) {
            appendSeparator();
            entries.append(property.serialized());
        }

        @Override
        public String serialized() {
            return String.format("[%s]", entries.toString());
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
