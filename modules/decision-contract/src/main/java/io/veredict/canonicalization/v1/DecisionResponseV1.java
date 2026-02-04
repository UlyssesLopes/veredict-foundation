package io.veredict.contract.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisionResponseV1(
        @JsonProperty(value = "contractVersion", required = true)
        String contractVersion,

        @JsonProperty(value = "decisionId", required = true)
        String decisionId,

        @JsonProperty(value = "contextHash", required = true)
        String contextHash,

        @JsonProperty(value = "bundleHash", required = true)
        String bundleHash,

        @JsonProperty(value = "outcome", required = true)
        DecisionOutcome outcome,

        @JsonProperty("trace")
        Map<String, Object> trace
) {
    public static final String VERSION = "1.0";

    public DecisionResponseV1 {
        if (contractVersion == null || contractVersion.isBlank()) throw new IllegalArgumentException("contractVersion is required");
        if (decisionId == null || decisionId.isBlank()) throw new IllegalArgumentException("decisionId is required");
        if (contextHash == null || contextHash.isBlank()) throw new IllegalArgumentException("contextHash is required");
        if (bundleHash == null || bundleHash.isBlank()) throw new IllegalArgumentException("bundleHash is required");
        if (outcome == null) throw new IllegalArgumentException("outcome is required");
    }
}
