package io.veredict.bundlecontract.v1.api;

public record BundleMetadataV1(
        String tenant,
        String author,
        String notes
) {
    public BundleMetadataV1 {
        if (tenant != null && tenant.isBlank()) throw new IllegalArgumentException("tenant blank");
        if (author != null && author.isBlank()) throw new IllegalArgumentException("author blank");
        if (notes != null && notes.isBlank()) throw new IllegalArgumentException("notes blank");
    }
}
