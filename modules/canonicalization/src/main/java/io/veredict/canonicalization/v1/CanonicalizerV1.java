package io.veredict.canonicalization.v1;

/**
 * Canonicalization v1: deterministic JSON canonical form (JCS-like).

 * Input/Output are UTF-8 JSON bytes.
 */
public interface CanonicalizerV1 {
    byte[] canonicalize(byte[] jsonUtf8);
}
