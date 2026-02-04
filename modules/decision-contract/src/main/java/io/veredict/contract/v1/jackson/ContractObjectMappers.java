package io.veredict.contract.v1.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Contract-safe, deterministic JSON mapper for Veredict Decision Contract.

 * Notes:
 * - Sorting is enabled so re-serialization is stable.
 * - Non-numeric numbers are explicitly disallowed.
 */
public class ContractObjectMappers {

    private ContractObjectMappers() {
    }

    public static ObjectMapper decisionContractV1() {
        JsonFactory factory = JsonFactory.builder()
                .disable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS)
                .build();

        return JsonMapper.builder(factory)
                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, false)
                .build();
    }
}
