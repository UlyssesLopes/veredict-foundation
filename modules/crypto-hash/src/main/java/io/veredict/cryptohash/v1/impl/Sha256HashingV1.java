package io.veredict.cryptohash.v1.impl;

import io.veredict.contract.v1.api.CanonicalizerV1;
import io.veredict.contract.v1.impl.JcsLikeCanonicalizerV1;
import io.veredict.cryptohash.v1.internal.HashingException;
import io.veredict.cryptohash.v1.internal.Hex;
import io.veredict.cryptohash.v1.api.HashingV1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class Sha256HashingV1 implements HashingV1 {

    private final CanonicalizerV1 canonicalizer;

    public Sha256HashingV1() {
        this(new JcsLikeCanonicalizerV1());
    }

    public Sha256HashingV1(CanonicalizerV1 canonicalizer) {
        if (canonicalizer == null) throw new IllegalArgumentException("canonicalizer is required");
        this.canonicalizer = canonicalizer;
    }

    @Override
    public String sha256Hex(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            return Hex.toHexLower(digest);
        } catch (Exception e) {
            throw new HashingException("SHA-256 hashing failed", e);
        }
    }

    @Override
    public String contextHash(byte[] contextJsonUtf8) {
        byte[] canonical = canonicalizer.canonicalize(contextJsonUtf8);
        return sha256Hex(canonical);
    }

    @Override
    public String bundleHash(byte[] bundleRefJsonUtf8) {
        byte[] canonical = canonicalizer.canonicalize(bundleRefJsonUtf8);
        return sha256Hex(canonical);
    }

    /**
     * decisionId v1 = sha256Hex( contextHash + ":" + bundleHash ) using UTF-8 bytes.
     */
    @Override
    public String decisionId(String contextHashHex, String bundleHashHex) {
        if (contextHashHex == null || contextHashHex.isBlank()) throw new IllegalArgumentException("contextHashHex is required");
        if (bundleHashHex == null || bundleHashHex.isBlank()) throw new IllegalArgumentException("bundleHashHex is required");

        String material = contextHashHex + ":" + bundleHashHex;
        return sha256Hex(material.getBytes(StandardCharsets.UTF_8));
    }
}
