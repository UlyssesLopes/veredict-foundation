package io.veredict.cryptohash.v1;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CryptoHashV1DeterminismTest {

    private static final HashingV1 H = new Sha256HashingV1();

    @Test
    void hashes_mustMatchGoldenExactly() {
        byte[] context = Resource.bytes("golden/crypto-hash/v1/context.input.json");
        byte[] bundleRef = Resource.bytes("golden/crypto-hash/v1/bundleRef.input.json");

        String expectedContextHash = Resource.text("golden/crypto-hash/v1/context.hash.txt");
        String expectedBundleHash = Resource.text("golden/crypto-hash/v1/bundle.hash.txt");
        String expectedDecisionId = Resource.text("golden/crypto-hash/v1/decision.id.txt");

        String contextHash = H.contextHash(context);
        String bundleHash = H.bundleHash(bundleRef);
        String decisionId = H.decisionId(contextHash, bundleHash);

        assertEquals(expectedContextHash, contextHash, "contextHash mismatch");
        assertEquals(expectedBundleHash, bundleHash, "bundleHash mismatch");
        assertEquals(expectedDecisionId, decisionId, "decisionId mismatch");
    }

    static final class Resource {
        static byte[] bytes(String path) {
            try (var in = CryptoHashV1DeterminismTest.class.getClassLoader().getResourceAsStream(path)) {
                if (in == null) throw new IllegalArgumentException("Missing resource: " + path);
                return in.readAllBytes();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read resource: " + path, e);
            }
        }

        static String text(String path) {
            return normalizeUtf8(bytes(path));
        }

        private static String normalizeUtf8(byte[] bytes) {
            String s = new String(bytes, StandardCharsets.UTF_8);

            if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
                s = s.substring(1);
            }

            return s.replace("\r\n", "\n").trim();
        }
    }
}
