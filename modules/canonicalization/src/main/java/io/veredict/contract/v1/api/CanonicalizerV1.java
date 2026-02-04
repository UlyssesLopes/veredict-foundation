package io.veredict.contract.v1.api;

/**
 * Canonicalization v1: deterministic JSON canonical form (JCS-like).

 * Input/Output are UTF-8 JSON bytes.
 */
public interface CanonicalizerV1 {
    byte[] canonicalize(byte[] jsonUtf8);
}
