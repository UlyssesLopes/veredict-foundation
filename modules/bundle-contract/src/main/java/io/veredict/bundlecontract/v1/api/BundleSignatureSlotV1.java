package io.veredict.bundlecontract.v1.api;

public record BundleSignatureSlotV1(
        String algorithm,
        String keyId,
        String signature
) {
    public BundleSignatureSlotV1 {
        if (algorithm != null && algorithm.isBlank()) throw new IllegalArgumentException("algorithm blank");
        if (keyId != null && keyId.isBlank()) throw new IllegalArgumentException("keyId blank");
        if (signature != null && signature.isBlank()) throw new IllegalArgumentException("signature blank");
    }
}
