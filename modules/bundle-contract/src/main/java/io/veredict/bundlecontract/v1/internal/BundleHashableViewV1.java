package io.veredict.bundlecontract.v1.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.veredict.bundlecontract.v1.api.BundleContractVersion;

import java.util.Objects;

/**
 * Defines the exact JSON subset used to compute bundleHash v1 (hashable view).

 * Inclusions:
 * - contractVersion, bundleName, bundleVersion, createdAt, metadata, bundle

 * Exclusions:
 * - bundleHash (self-referential)
 * - signature (placeholder; not part of integrity of rules payload)
 */
public final class BundleHashableViewV1 {

    private BundleHashableViewV1() {}

    public static ObjectNode fromBundleJson(ObjectMapper mapper, JsonNode inputBundleJson) {
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(inputBundleJson, "inputBundleJson");

        if (!inputBundleJson.isObject()) {
            throw new IllegalArgumentException("bundle json must be an object");
        }

        ObjectNode root = (ObjectNode) inputBundleJson;
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
