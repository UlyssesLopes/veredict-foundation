package io.veredict.cryptohash.v1;

final class Hex {

    private static final char[] LOWER = "0123456789abcdef".toCharArray();

    private Hex() {
    }

    static String toHexLower(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("bytes is required");

        char[] out = new char[bytes.length * 2];
        int i = 0;
        for (byte b : bytes) {
            int v = b & 0xFF;
            out[i++] = LOWER[v >>> 4];
            out[i++] = LOWER[v & 0x0F];
        }
        return new String(out);
    }
}
