package io.veredict.contract.v1.internal;

public final class CanonicalizationException extends RuntimeException {
    public CanonicalizationException(String message) {
        super(message);
    }

    public CanonicalizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
