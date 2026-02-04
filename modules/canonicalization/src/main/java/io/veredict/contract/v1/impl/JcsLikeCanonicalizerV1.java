package io.veredict.contract.v1.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.veredict.contract.v1.internal.CanonicalizationException;
import io.veredict.contract.v1.api.CanonicalizerV1;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Deterministic JSON canonicalization (JCS-like) for Veredict Foundation v1.

 * Rules:
 * - Objects: keys sorted lexicographically (Unicode code points)
 * - Arrays: preserve order
 * - Strings: canonical JSON escaping by generator
 * - Numbers: deterministic BigDecimal plain string; normalize -0 to 0
 * - No whitespace
 * - Disallow non-numeric numbers (NaN/Infinity)

 * Note: RFC8785-inspired; strict compliance can be formalized in v2 if needed.
 */
public final class JcsLikeCanonicalizerV1 implements CanonicalizerV1 {

    private final ObjectMapper mapper;

    public JcsLikeCanonicalizerV1() {
        JsonFactory factory = JsonFactory.builder()
                .configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS, false)
                .build();

        this.mapper = JsonMapper.builder(factory).build();
    }

    @Override
    public byte[] canonicalize(byte[] jsonUtf8) {
        if (jsonUtf8 == null) throw new IllegalArgumentException("jsonUtf8 is required");

        final JsonNode root;
        try {
            root = mapper.readTree(jsonUtf8);
        } catch (Exception e) {
            throw new CanonicalizationException("Invalid JSON input", e);
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator g = mapper.getFactory().createGenerator(out);

            writeNode(g, root);

            g.flush();
            g.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new CanonicalizationException("Canonicalization failed", e);
        }
    }

    private void writeNode(JsonGenerator g, JsonNode n) throws Exception {
        if (n == null || n.isNull()) {
            g.writeNull();
            return;
        }

        if (n.isObject()) {
            g.writeStartObject();

            List<String> names = new ArrayList<>();
            Iterator<String> it = n.fieldNames();
            while (it.hasNext()) names.add(it.next());
            names.sort(Comparator.naturalOrder());

            for (String name : names) {
                g.writeFieldName(name);
                writeNode(g, n.get(name));
            }

            g.writeEndObject();
            return;
        }

        if (n.isArray()) {
            g.writeStartArray();
            for (JsonNode child : n) {
                writeNode(g, child);
            }
            g.writeEndArray();
            return;
        }

        if (n.isTextual()) {
            g.writeString(n.textValue());
            return;
        }

        if (n.isBoolean()) {
            g.writeBoolean(n.booleanValue());
            return;
        }

        if (n.isNumber()) {
            writeNumber(g, n.decimalValue());
            return;
        }

        g.writeString(n.asText());
    }

    private void writeNumber(JsonGenerator g, BigDecimal value) throws Exception {
        if (value == null) {
            g.writeNull();
            return;
        }

        // Normalize -0 => 0
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            g.writeNumber("0");
            return;
        }

        BigDecimal normalized = value.stripTrailingZeros();
        String s = normalized.toPlainString();

        if ("-0".equals(s)) s = "0";

        g.writeNumber(s);
    }
}
