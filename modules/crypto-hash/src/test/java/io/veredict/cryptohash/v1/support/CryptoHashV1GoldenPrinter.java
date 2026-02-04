package io.veredict.cryptohash.v1.support;

import io.veredict.cryptohash.v1.HashingV1;
import io.veredict.cryptohash.v1.Sha256HashingV1;

public final class CryptoHashV1GoldenPrinter {

    private CryptoHashV1GoldenPrinter() {}

    public static void main(String[] args) {
        HashingV1 h = new Sha256HashingV1();

        byte[] context = Resource.bytes("golden/crypto-hash/v1/context.input.json");
        byte[] bundleRef = Resource.bytes("golden/crypto-hash/v1/bundleRef.input.json");

        String contextHash = h.contextHash(context);
        String bundleHash = h.bundleHash(bundleRef);
        String decisionId = h.decisionId(contextHash, bundleHash);

        System.out.println("CONTEXT_HASH=" + contextHash);
        System.out.println("BUNDLE_HASH=" + bundleHash);
        System.out.println("DECISION_ID=" + decisionId);
    }

    static final class Resource {
        static byte[] bytes(String path) {
            try (var in = CryptoHashV1GoldenPrinter.class.getClassLoader().getResourceAsStream(path)) {
                if (in == null) throw new IllegalArgumentException("Missing resource: " + path);
                return in.readAllBytes();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read resource: " + path, e);
            }
        }
    }
}
