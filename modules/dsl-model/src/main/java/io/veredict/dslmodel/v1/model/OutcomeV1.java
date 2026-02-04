package io.veredict.dslmodel.v1.model;

import io.veredict.dslmodel.v1.internal.Require;

public record OutcomeV1(
        OutcomeStatus status,
        String reasonCode
) {
    public OutcomeV1 {
        Require.nonNull(status, "status");
        // reasonCode is optional; but if provided must be non-blank
        if (reasonCode != null && reasonCode.isBlank()) {
            throw new IllegalArgumentException("reasonCode must be non-blank when provided");
        }
    }

    public static OutcomeV1 allow() {
        return new OutcomeV1(OutcomeStatus.ALLOW, null);
    }

    public static OutcomeV1 deny(String reasonCode) {
        return new OutcomeV1(OutcomeStatus.DENY, Require.nonBlank(reasonCode, "reasonCode"));
    }

    public static OutcomeV1 requireAction(String reasonCode) {
        return new OutcomeV1(OutcomeStatus.REQUIRE_ACTION, Require.nonBlank(reasonCode, "reasonCode"));
    }
}
