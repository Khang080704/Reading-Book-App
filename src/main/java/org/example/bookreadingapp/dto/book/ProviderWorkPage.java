package org.example.bookreadingapp.dto.book;

import java.util.List;

public record ProviderWorkPage(
        List<WorkDTO> works,
        boolean hasNext,
        int nextOffset,
        long totalElement
) {
}
