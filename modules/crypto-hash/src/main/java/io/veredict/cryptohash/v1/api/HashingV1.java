package io.veredict.cryptohash.v1.api;

public interface HashingV1 {

    String sha256Hex(byte[] bytes);

    String contextHash(byte[] contextJsonUtf8);

    String bundleHash(byte[] bundleRefJsonUtf8);

    String decisionId(String contextHashHex, String bundleHashHex);

}
