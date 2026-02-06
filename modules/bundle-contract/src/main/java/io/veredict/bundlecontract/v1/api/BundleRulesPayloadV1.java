package io.veredict.bundlecontract.v1.api;

import java.util.List;
import java.util.Objects;

/**
 * Payload base: AST-only vindo do dsl-model v1 (engine-agnostic).

 * Aqui eu uso Object como placeholder de tipo do AST porque eu não sei o nome exato
 * do root AST no teu dsl-model (ex: DslAstNodeV1 / ProgramV1 / ExprV1 etc).

 * NA PRÓXIMA ETAPA você troca Object pelo tipo real do dsl-model v1.
 */
public record BundleRulesPayloadV1(
        List<Object> rules
) {
    public BundleRulesPayloadV1 {
        Objects.requireNonNull(rules, "rules");
        if (rules.isEmpty()) throw new IllegalArgumentException("rules empty");
    }
}
