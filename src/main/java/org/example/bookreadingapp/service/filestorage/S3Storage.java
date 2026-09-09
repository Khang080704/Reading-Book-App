package org.example.bookreadingapp.service.filestorage;

import org.example.bookreadingapp.Enum.StorageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class S3Storage implements BookStorage {
    private final ResourceLoader resourceLoader;

    @Autowired
    public S3Storage(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String readFIleFromS3(String bucketName, String fileName) {
        try {
            String s3Path = String.format("s3://%s/%s", bucketName, fileName);
            Resource resource = resourceLoader.getResource(s3Path);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to read file from S3: " + e.getMessage(), e);
        }

    }

    @Override
    public boolean support(StorageType storageType) {
        return storageType == StorageType.S3;
    }

    @Override
    public Resource load(String location) {
        try {
            String s3Path = location.startsWith("s3://") ? location : "s3://" + location;
            Resource resource = resourceLoader.getResource(s3Path);

            if (!resource.exists()) {
                throw new RuntimeException("File does not exist on S3 at location: " + s3Path);
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
