package io.veredict.contract.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisionOutcome(
        @JsonProperty(value = "status", required = true)
        DecisionStatus status,

        @JsonProperty("reasonCode")
        String reasonCode
) {
    public DecisionOutcome {
        if (status == null) throw new IllegalArgumentException("status is required");
    }
}
