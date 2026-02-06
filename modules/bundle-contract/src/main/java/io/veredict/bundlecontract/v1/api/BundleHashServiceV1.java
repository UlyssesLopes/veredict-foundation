package io.veredict.bundlecontract.v1.api;

import com.fasterxml.jackson.databind.JsonNode;

public interface BundleHashServiceV1 {

    /**
     * Gera o canonical JSON (UTF-8) da hashable view.
     */
    byte[] canonicalizeForHash(JsonNode inputBundleJson);

    /**
     * Retorna sha-256 hex lowercase da canonicalização.
     */
    String computeBundleHash(JsonNode inputBundleJson);
}
