package io.veredict.contract.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisionRequestV1(
        @JsonProperty(value = "contractVersion", required = true)
        String contractVersion,

        @JsonProperty(value = "ruleBundleRef", required = true)
        RuleBundleRef ruleBundleRef,

        @JsonProperty(value = "context", required = true)
        Map<String, Object> context,

        @JsonProperty("metadata")
        Map<String, Object> metadata
) {
    public static final String VERSION = "1.0";

    public DecisionRequestV1 {
        if (contractVersion == null || contractVersion.isBlank()) throw new IllegalArgumentException("contractVersion is required");
        if (ruleBundleRef == null) throw new IllegalArgumentException("ruleBundleRef is required");
        if (context == null) throw new IllegalArgumentException("context is required");
    }
}
