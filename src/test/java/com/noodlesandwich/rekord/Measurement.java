package com.noodlesandwich.rekord;

public final class Measurement {
    public static MeasurementBuilder of(int value) {
        return new MeasurementBuilder(value);
    }

    public static class MeasurementBuilder {
        private final int value;

        private MeasurementBuilder(int value) {
            this.value = value;
        }

        public Volume ml() {
            return new Volume(value);
        }

        public Volume l() {
            return new Volume(value * 1000);
        }

        public Length cm() {
            return new Length(value);
        }
    }

    public static final class Volume {
        private final int value;

        public Volume(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Volume)) return false;

            Volume volume = (Volume) o;
            return value == volume.value;

        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return value + "ml";
        }
    }

    public static final class Length {
        private final int value;

        public Length(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Length)) return false;

            Length length = (Length) o;
            return value == length.value;

        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return value + "cm";
        }
    }
}
