package io.veredict.canonicalization.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RuleBundleRef(
        @JsonProperty(value = "bundleName", required = true)
        String bundleName,

        @JsonProperty(value = "bundleVersion", required = true)
        String bundleVersion
) {
    public RuleBundleRef {
        if (bundleName == null || bundleName.isBlank()) throw new IllegalArgumentException("bundleName is required");
        if (bundleVersion == null || bundleVersion.isBlank()) throw new IllegalArgumentException("bundleVersion is required");
    }
}
