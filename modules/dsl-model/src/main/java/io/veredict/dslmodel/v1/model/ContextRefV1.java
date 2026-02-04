package io.veredict.dslmodel.v1.model;

import io.veredict.dslmodel.v1.internal.Require;

/**
 * Path-based reference into context data (engine-agnostic).
 * Examples: "user.id", "kyc.level", "tags".
 */
public record ContextRefV1(String path) {
    public ContextRefV1 {
        Require.nonBlank(path, "path");
        // Determinism guard: no surrounding whitespace
        if (!path.equals(path.trim())) throw new IllegalArgumentException("path must be trimmed");
    }
}
