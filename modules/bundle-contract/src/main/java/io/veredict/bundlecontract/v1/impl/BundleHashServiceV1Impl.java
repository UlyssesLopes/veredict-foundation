package io.veredict.bundlecontract.v1.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.veredict.bundlecontract.v1.api.BundleContractVersion;
import io.veredict.bundlecontract.v1.api.BundleHashServiceV1;

import io.veredict.contract.v1.api.CanonicalizerV1;
import io.veredict.cryptohash.v1.api.HashingV1;

import java.util.Objects;

public final class BundleHashServiceV1Impl implements BundleHashServiceV1 {

    private final ObjectMapper mapper;
    private final CanonicalizerV1 canonicalizer;
    private final HashingV1 hashing;

    public BundleHashServiceV1Impl(ObjectMapper mapper,
                                   CanonicalizerV1 canonicalizer,
                                   HashingV1 hashing) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
        this.canonicalizer = Objects.requireNonNull(canonicalizer, "canonicalizer");
        this.hashing = Objects.requireNonNull(hashing, "hashing");
    }

    @Override
    public byte[] canonicalizeForHash(JsonNode inputBundleJson) {
        Objects.requireNonNull(inputBundleJson, "inputBundleJson");
        ObjectNode hashable = toHashableView(inputBundleJson);

        try {
            // 1) serializa a hashable view em JSON UTF-8 (entrada do canonicalizer)
            byte[] jsonUtf8 = mapper.writeValueAsBytes(hashable);

            // 2) canonicaliza determinísticamente (JCS-like do EPIC-001)
            return canonicalizer.canonicalize(jsonUtf8);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize hashable bundle view", e);
        }
    }

    @Override
    public String computeBundleHash(JsonNode inputBundleJson) {
        byte[] canonicalUtf8 = canonicalizeForHash(inputBundleJson);
        return hashing.sha256Hex(canonicalUtf8);
    }

    /**
     * Hashable view rules:
     * - inclui: contractVersion, bundleName, bundleVersion, createdAt, metadata, bundle
     * - exclui: bundleHash, signature
     */
    private ObjectNode toHashableView(JsonNode input) {
        if (!input.isObject()) throw new IllegalArgumentException("bundle json must be an object");

        ObjectNode root = (ObjectNode) input;
        ObjectNode out = mapper.createObjectNode();

        JsonNode contractVersion = root.get("contractVersion");
        if (contractVersion == null || !contractVersion.isTextual()) {
            throw new IllegalArgumentException("contractVersion missing/invalid");
        }
        if (!BundleContractVersion.BUNDLE_V1.equals(contractVersion.asText())) {
            throw new IllegalArgumentException("unsupported contractVersion: " + contractVersion.asText());
        }

        out.put("contractVersion", BundleContractVersion.BUNDLE_V1);

        copyRequiredText(root, out, "bundleName");
        copyRequiredText(root, out, "bundleVersion");
        copyRequiredText(root, out, "createdAt");

        if (root.has("metadata")) out.set("metadata", root.get("metadata"));

        if (!root.has("bundle")) throw new IllegalArgumentException("bundle missing");
        out.set("bundle", root.get("bundle"));

        return out;
    }

    private static void copyRequiredText(ObjectNode in, ObjectNode out, String field) {
        JsonNode n = in.get(field);
        if (n == null) throw new IllegalArgumentException(field + " missing");
        if (!n.isTextual()) throw new IllegalArgumentException(field + " must be string");
        if (n.asText().isBlank()) throw new IllegalArgumentException(field + " blank");
        out.set(field, n);
    }
}
