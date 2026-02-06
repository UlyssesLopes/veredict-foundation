package io.veredict.bundlecontract.v1.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.veredict.bundlecontract.v1.api.BundleHashServiceV1;
import io.veredict.bundlecontract.v1.internal.BundleHashableViewV1;
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

        ObjectNode hashable = BundleHashableViewV1.fromBundleJson(mapper, inputBundleJson);

        try {
            byte[] jsonUtf8 = mapper.writeValueAsBytes(hashable);
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
}
