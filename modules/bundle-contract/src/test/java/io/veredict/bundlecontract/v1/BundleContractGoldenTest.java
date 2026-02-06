package io.veredict.bundlecontract.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.veredict.bundlecontract.v1.api.BundleHashServiceV1;
import io.veredict.bundlecontract.v1.impl.BundleHashServiceV1Impl;
import io.veredict.contract.v1.api.CanonicalizerV1;
import io.veredict.contract.v1.impl.JcsLikeCanonicalizerV1;
import io.veredict.cryptohash.v1.api.HashingV1;
import io.veredict.cryptohash.v1.impl.Sha256HashingV1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

final class BundleContractGoldenTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void golden_input_to_canonical_to_hash() throws Exception {
        JsonNode input = readJson("/golden/bundlecontract/v1/input.bundle.json");

        CanonicalizerV1 canonicalizer = new JcsLikeCanonicalizerV1();
        HashingV1 hashing = new Sha256HashingV1();

        BundleHashServiceV1 svc = new BundleHashServiceV1Impl(mapper, canonicalizer, hashing);

        byte[] canonicalUtf8 = svc.canonicalizeForHash(input);
        String canonical = new String(canonicalUtf8, StandardCharsets.UTF_8);

        String expectedCanonical = readText("/golden/bundlecontract/v1/expected.canonical.json");
        assertEquals(expectedCanonical, canonical, "canonical json must match golden");

        String hash = svc.computeBundleHash(input);

        String expectedHash = readText("/golden/bundlecontract/v1/expected.bundleHash.hex");
        assertEquals(expectedHash, hash, "bundleHash must match golden");
    }

    private JsonNode readJson(String cp) throws Exception {
        try (var is = BundleContractGoldenTest.class.getResourceAsStream(cp)) {
            assertNotNull(is, "missing classpath resource: " + cp);
            return mapper.readTree(is);
        }
    }

    private String readText(String cp) throws Exception {
        try (var is = BundleContractGoldenTest.class.getResourceAsStream(cp)) {
            assertNotNull(is, "missing classpath resource: " + cp);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }
}
