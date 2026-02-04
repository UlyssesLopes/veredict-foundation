package io.veredict.cryptohash.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class Sha256KnownVectorsTest {

    private static final HashingV1 H = new Sha256HashingV1();

    @Test
    void sha256_emptyString() {
        assertEquals(
                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                H.sha256Hex(new byte[0])
        );
    }

    @Test
    void sha256_abc() {
        assertEquals(
                "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                H.sha256Hex("abc".getBytes(StandardCharsets.UTF_8))
        );
    }
}
