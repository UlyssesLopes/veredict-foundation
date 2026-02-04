package io.veredict.dslmodel.v1.model;

import io.veredict.dslmodel.v1.internal.Require;

import java.util.List;
import java.util.Map;

public record RuleBundleV1(
        String bundleName,
        String bundleVersion,
        List<RuleV1> rules,
        Map<String, String> metadata
) {
    public RuleBundleV1 {
        Require.nonBlank(bundleName, "bundleName");
        Require.nonBlank(bundleVersion, "bundleVersion");
        Require.noNullElements(rules, "rules");
        rules = List.copyOf(rules);

        metadata = (metadata == null) ? Map.of() : Map.copyOf(metadata);

        // Determinism guard: forbid null keys/values and blank keys
        if (metadata.keySet().stream().anyMatch(k -> k == null || k.isBlank())) {
            throw new IllegalArgumentException("metadata keys must be non-null and non-blank");
        }
        if (metadata.values().stream().anyMatch(v -> v == null)) {
            throw new IllegalArgumentException("metadata values must be non-null");
        }
    }
}
