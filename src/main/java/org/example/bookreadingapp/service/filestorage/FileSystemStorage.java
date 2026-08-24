package org.example.bookreadingapp.service.filestorage;

import org.example.bookreadingapp.Enum.StorageType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class FileSystemStorage implements BookStorage{
    private final Path rootPath;

    public FileSystemStorage(
            @Value("${book.storage.filesystem.root}") String root
    ) {
        this.rootPath = Path.of(root);
    }

    @Override
    public boolean support(StorageType storageType) {
        return storageType == StorageType.FILE_SYSTEM;
    }

    @Override
    public Resource load(String location, StorageType storageType) {
        Path file = rootPath
                        .resolve(location)
                        .normalize();

        return new FileSystemResource(file);
    }
}
