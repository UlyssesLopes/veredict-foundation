package io.veredict.canonicalization.v1;

public final class CanonicalizationException extends RuntimeException {
    public CanonicalizationException(String message) {
        super(message);
    }

    public CanonicalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
