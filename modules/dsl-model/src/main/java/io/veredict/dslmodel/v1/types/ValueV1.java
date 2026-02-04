package io.veredict.dslmodel.v1.types;

import io.veredict.dslmodel.v1.internal.Require;

import java.math.BigDecimal;
import java.time.Instant;

public sealed interface ValueV1 permits
        ValueV1.NullValue,
        ValueV1.BoolValue,
        ValueV1.NumberValue,
        ValueV1.StringValue,
        ValueV1.InstantValue {

    ValueType type();

    record NullValue() implements ValueV1 {
        @Override public ValueType type() { return ValueType.NULL; }
    }

    record BoolValue(boolean value) implements ValueV1 {
        @Override public ValueType type() { return ValueType.BOOLEAN; }
    }

    record NumberValue(BigDecimal value) implements ValueV1 {
        public NumberValue {
            Require.nonNull(value, "value");
        }
        @Override public ValueType type() { return ValueType.NUMBER; }
    }

    record StringValue(String value) implements ValueV1 {
        public StringValue {
            Require.nonNull(value, "value");
        }
        @Override public ValueType type() { return ValueType.STRING; }
    }

    record InstantValue(Instant value) implements ValueV1 {
        public InstantValue {
            Require.nonNull(value, "value");
        }
        @Override public ValueType type() { return ValueType.INSTANT; }
    }
}
