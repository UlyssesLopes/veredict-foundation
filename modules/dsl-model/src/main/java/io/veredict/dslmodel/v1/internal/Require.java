package io.veredict.dslmodel.v1.internal;

import java.util.Collection;
import java.util.Objects;

/**
 * Internal validation helpers (NOT part of the public API surface).
 * Kept public to allow reuse across v1 subpackages (ast/model/types).
 * Compatibility is not guaranteed for consumers outside this module.
 */
public final class Require {

    private Require() {}

    public static <T> T nonNull(T v, String name) {
        return Objects.requireNonNull(v, name + " is required");
    }

    public static String nonBlank(String v, String name) {
        nonNull(v, name);
        if (v.isBlank()) throw new IllegalArgumentException(name + " must be non-blank");
        return v;
    }

    public static <T extends Collection<?>> T noNullElements(T c, String name) {
        nonNull(c, name);
        for (Object e : c) {
            if (e == null) throw new IllegalArgumentException(name + " must not contain null elements");
        }
        return c;
    }
}
