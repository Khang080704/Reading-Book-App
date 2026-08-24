package org.example.bookreadingapp.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreadingapp.service.filestorage.BookStorageResolver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookParsingService {
    private final BookStorageResolver storageResolver;
}
