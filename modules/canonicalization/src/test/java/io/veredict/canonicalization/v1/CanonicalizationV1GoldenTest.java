package io.veredict.canonicalization.v1;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CanonicalizationV1GoldenTest {

    private static final CanonicalizerV1 C14N = new JcsLikeCanonicalizerV1();

    @Test
    void simpleObject_mustMatchGolden() {
        assertGolden(
                "golden/canonicalization/v1/input.simple.json",
                "golden/canonicalization/v1/output.simple.canonical.json"
        );
    }

    @Test
    void numbers_mustMatchGolden() {
        assertGolden(
                "golden/canonicalization/v1/input.numbers.json",
                "golden/canonicalization/v1/output.numbers.canonical.json"
        );
    }

    private static void assertGolden(String inputPath, String expectedPath) {
        byte[] in = Resource.bytes(inputPath);
        byte[] expected = Resource.bytes(expectedPath);

        byte[] out = C14N.canonicalize(in);

        String exp = normalizeUtf8(expected);
        String act = normalizeUtf8(out);

        assertEquals(exp, act, "Golden mismatch for input: " + inputPath);
    }

    private static String normalizeUtf8(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8);

        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            s = s.substring(1);
        }

        return s.replace("\r\n", "\n").trim();
    }

    static final class Resource {
        static byte[] bytes(String path) {
            try (var in = CanonicalizationV1GoldenTest.class.getClassLoader().getResourceAsStream(path)) {
                if (in == null) throw new IllegalArgumentException("Missing resource: " + path);
                return in.readAllBytes();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read resource: " + path, e);
            }
        }
    }
}
