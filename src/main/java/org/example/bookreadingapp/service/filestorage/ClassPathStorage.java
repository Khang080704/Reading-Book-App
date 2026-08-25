package org.example.bookreadingapp.service.filestorage;

import org.example.bookreadingapp.Enum.StorageType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ClassPathStorage implements BookStorage{
    @Override
    public boolean support(StorageType storageType) {
        return storageType == StorageType.CLASSPATH;
    }

    @Override
    public Resource load(String location) {
        return new ClassPathResource(location);
    }
}
