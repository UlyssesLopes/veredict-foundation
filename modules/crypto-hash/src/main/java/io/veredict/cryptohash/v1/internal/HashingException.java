package io.veredict.cryptohash.v1.internal;

public final class HashingException extends RuntimeException {
    public HashingException(String message) {
        super(message);
    }

    public HashingException(String message, Throwable cause) {
        super(message, cause);
    }
}
