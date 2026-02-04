package io.veredict.contract.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class DecisionContractGoldenTest {

    private static final ObjectMapper MAPPER = ContractObjectMappers.decisionContractV1();

    @Test
    void requestMin_mustRoundTripExactly() throws Exception {
        assertRoundTrip("golden.contract.v1/request.min.json", DecisionRequestV1.class);
    }

    @Test
    void requestFull_mustRoundTripExactly() throws Exception {
        assertRoundTrip("golden.contract.v1/request.full.json", DecisionRequestV1.class);
    }

    @Test
    void responseMin_mustRoundTripExactly() throws Exception {
        assertRoundTrip("golden.contract.v1/response.min.json", DecisionResponseV1.class);
    }

    @Test
    void responseFull_mustRoundTripExactly() throws Exception {
        assertRoundTrip("golden.contract.v1/response.full.json", DecisionResponseV1.class);
    }

    private static <T> void assertRoundTrip(String resourcePath, Class<T> type) throws Exception {
        byte[] goldenBytes = Resource.bytes(resourcePath);
        T obj = MAPPER.readValue(goldenBytes, type);
        byte[] outBytes = MAPPER.writeValueAsBytes(obj);

        String expected = normalizeUtf8(goldenBytes);
        String actual = normalizeUtf8(outBytes);

        assertEquals(expected, actual, "Golden mismatch for: " + resourcePath);
    }

    private static String normalizeUtf8(byte[] bytes) {
        String s = new String(bytes, StandardCharsets.UTF_8);

        // Remove UTF-8 BOM if present
        if (!s.isEmpty() && s.charAt(0) == '\uFEFF') {
            s = s.substring(1);
        }

        // Normalize line endings and trim only outer whitespace/newlines
        s = s.replace("\r\n", "\n").trim();

        return s;
    }

    static final class Resource {
        static byte[] bytes(String path) {
            try (var in = DecisionContractGoldenTest.class.getClassLoader().getResourceAsStream(path)) {
                if (in == null) throw new IllegalArgumentException("Missing resource: " + path);
                return in.readAllBytes();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to read resource: " + path, e);
            }
        }
    }

}
