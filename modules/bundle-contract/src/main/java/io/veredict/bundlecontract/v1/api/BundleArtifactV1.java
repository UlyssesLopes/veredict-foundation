package io.veredict.bundlecontract.v1.api;

import java.time.Instant;
import java.util.Objects;

/**
 * Bundle Artifact transportável entre Control Plane (publish) e Data Plane (consume).

 * Hashing determinístico:
 * - bundleHash é derivado da "hashable view" (exclui bundleHash e signature).
 */
public record BundleArtifactV1(
        String contractVersion,     // obrigatório: "bundle.v1"
        String bundleName,          // obrigatório
        String bundleVersion,       // obrigatório (string semver-like)
        Instant createdAt,          // obrigatório
        String bundleHash,          // obrigatório (hex lowercase) - calculado fora e injetado
        BundleMetadataV1 metadata,  // opcional
        BundleSignatureSlotV1 signature, // opcional placeholder (não implementado)
        BundleRulesPayloadV1 bundle // obrigatório
) {
    public BundleArtifactV1 {
        Objects.requireNonNull(contractVersion, "contractVersion");
        Objects.requireNonNull(bundleName, "bundleName");
        Objects.requireNonNull(bundleVersion, "bundleVersion");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(bundleHash, "bundleHash");
        Objects.requireNonNull(bundle, "bundle");

        if (!BundleContractVersion.BUNDLE_V1.equals(contractVersion)) {
            throw new IllegalArgumentException("unsupported contractVersion: " + contractVersion);
        }
        if (bundleName.isBlank()) throw new IllegalArgumentException("bundleName blank");
        if (bundleVersion.isBlank()) throw new IllegalArgumentException("bundleVersion blank");

        // hex lowercase defensável (64 chars p/ SHA-256)
        if (!bundleHash.matches("^[0-9a-f]{64}$")) {
            throw new IllegalArgumentException("bundleHash must be 64-char lowercase hex");
        }
    }
}
