package io.veredict.dslmodel.v1;

import io.veredict.dslmodel.v1.ast.BoolOp;
import io.veredict.dslmodel.v1.ast.CompareOp;
import io.veredict.dslmodel.v1.ast.ExprV1;
import io.veredict.dslmodel.v1.model.ContextRefV1;
import io.veredict.dslmodel.v1.model.OutcomeV1;
import io.veredict.dslmodel.v1.model.RuleBundleV1;
import io.veredict.dslmodel.v1.model.RuleV1;
import io.veredict.dslmodel.v1.types.ValueV1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

class DslModelV1InvariantsTest {

    @Test
    void contextRef_mustRejectBlank() {
        assertThrows(IllegalArgumentException.class, () -> new ContextRefV1(" "));
    }

    @Test
    void values_mustBeNonNull() {
        assertThrows(NullPointerException.class, () -> new ValueV1.NumberValue(null));
        assertThrows(NullPointerException.class, () -> new ValueV1.InstantValue(null));
        assertThrows(NullPointerException.class, () -> new ValueV1.StringValue(null));
    }

    @Test
    void boolNary_mustHaveAtLeastTwoOperands() {
        assertThrows(IllegalArgumentException.class, () -> new ExprV1.BoolNary(BoolOp.AND, List.of(new ExprV1.LiteralBool(true))));
    }

    @Test
    void bundle_mustCopyCollectionsDefensively() {
        RuleV1 r = new RuleV1(
                "R-1",
                10,
                new ExprV1.RefEqualsValue(new ContextRefV1("country"), new ValueV1.StringValue("BR")),
                OutcomeV1.allow()
        );

        List<RuleV1> rules = new java.util.ArrayList<>(List.of(r));
        Map<String, String> md = new java.util.HashMap<>(Map.of("engine", "none"));

        RuleBundleV1 b = new RuleBundleV1("annual-update", "2026-01-01", rules, md);

        rules.clear();
        md.put("x", "y");

        assertEquals(1, b.rules().size());
        assertEquals("none", b.metadata().get("engine"));
        assertFalse(b.metadata().containsKey("x"));
    }

    @Test
    void canComposeExpressionTree() {
        ExprV1 e = new ExprV1.BoolNary(
                BoolOp.AND,
                List.of(
                        new ExprV1.Exists(new ContextRefV1("user.id")),
                        new ExprV1.Compare(
                                CompareOp.GTE,
                                new ExprV1.Operand.Ref(new ContextRefV1("kyc.level")),
                                new ExprV1.Operand.Val(new ValueV1.NumberValue(new BigDecimal("2")))
                        ),
                        new ExprV1.RefEqualsValue(new ContextRefV1("country"), new ValueV1.StringValue("BR")),
                        new ExprV1.RefEqualsValue(new ContextRefV1("createdAt"), new ValueV1.InstantValue(Instant.parse("2024-01-01T00:00:00Z")))
                )
        );

        assertNotNull(e);
    }
}
