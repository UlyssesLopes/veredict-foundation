package io.veredict.dslmodel.v1.model;

import io.veredict.dslmodel.v1.internal.Require;
import io.veredict.dslmodel.v1.ast.ExprV1;

public record RuleV1(
        String ruleId,
        int priority,
        ExprV1 when,
        OutcomeV1 then
) {
    public RuleV1 {
        Require.nonBlank(ruleId, "ruleId");
        Require.nonNull(when, "when");
        Require.nonNull(then, "then");
    }
}
