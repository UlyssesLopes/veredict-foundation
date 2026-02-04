package io.veredict.dslmodel.v1.ast;

import io.veredict.dslmodel.v1.model.ContextRefV1;
import io.veredict.dslmodel.v1.internal.Require;
import io.veredict.dslmodel.v1.types.ValueV1;

import java.util.List;

/**
 * Boolean expression AST for rules.
 * No parser, no evaluation semantics here.
 */
public sealed interface ExprV1 permits
        ExprV1.LiteralBool,
        ExprV1.Not,
        ExprV1.BoolNary,
        ExprV1.Compare,
        ExprV1.Exists,
        ExprV1.IsNull,
        ExprV1.RefEqualsValue {

    /**
     * Convenience leaf for hardcoded boolean (true/false) when composing AST programmatically.
     */
    record LiteralBool(boolean value) implements ExprV1 {}

    record Not(ExprV1 expr) implements ExprV1 {
        public Not {
            Require.nonNull(expr, "expr");
        }
    }

    /**
     * AND/OR with 2+ operands, deterministic order preserved as provided.
     */
    record BoolNary(BoolOp op, List<ExprV1> operands) implements ExprV1 {
        public BoolNary {
            Require.nonNull(op, "op");
            Require.noNullElements(operands, "operands");
            operands = List.copyOf(operands);
            if (operands.size() < 2) throw new IllegalArgumentException("operands must have at least 2 elements");
        }
    }

    /**
     * Compare two operands (Ref or Value).
     */
    record Compare(CompareOp op, Operand left, Operand right) implements ExprV1 {
        public Compare {
            Require.nonNull(op, "op");
            Require.nonNull(left, "left");
            Require.nonNull(right, "right");
        }
    }

    /**
     * Exists: checks the presence of a path in context (engine defines exact semantics).
     */
    record Exists(ContextRefV1 ref) implements ExprV1 {
        public Exists {
            Require.nonNull(ref, "ref");
        }
    }

    /**
     * IsNull: checks nullness of a path (engine defines exact semantics).
     */
    record IsNull(ContextRefV1 ref) implements ExprV1 {
        public IsNull {
            Require.nonNull(ref, "ref");
        }
    }

    /**
     * Common fast-path: ref == value (engine still defines semantics, but structure is explicit).
     */
    record RefEqualsValue(ContextRefV1 ref, ValueV1 value) implements ExprV1 {
        public RefEqualsValue {
            Require.nonNull(ref, "ref");
            Require.nonNull(value, "value");
        }
    }

    sealed interface Operand permits Operand.Ref, Operand.Val {
        record Ref(ContextRefV1 ref) implements Operand {
            public Ref {
                Require.nonNull(ref, "ref");
            }
        }
        record Val(ValueV1 value) implements Operand {
            public Val {
                Require.nonNull(value, "value");
            }
        }
    }
}
