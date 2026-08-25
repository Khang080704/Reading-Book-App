package org.example.bookreadingapp.service.filestorage;

import org.example.bookreadingapp.Enum.StorageType;
import org.springframework.core.io.Resource;

public interface BookStorage {
    boolean support(StorageType storageType);
    Resource load(String location);
}
