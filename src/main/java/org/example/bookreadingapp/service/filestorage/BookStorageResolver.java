package org.example.bookreadingapp.service.filestorage;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.Enum.StorageType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookStorageResolver {
    private final List<BookStorage> storages;

    public BookStorage resolve(StorageType storageType) {
        return storages.stream()
                .filter(storage -> storage.support(storageType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported storage: " + storageType
                        )
                );
    }

}
