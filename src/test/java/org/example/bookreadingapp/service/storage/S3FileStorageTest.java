package org.example.bookreadingapp.service.storage;

import io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration;
import io.awspring.cloud.autoconfigure.core.CredentialsProviderAutoConfiguration;
import io.awspring.cloud.autoconfigure.core.RegionProviderAutoConfiguration;
import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.example.bookreadingapp.service.filestorage.S3Storage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {S3Storage.class},
        initializers = ConfigDataApplicationContextInitializer.class
)
@ImportAutoConfiguration({
        AwsAutoConfiguration.class,
        S3AutoConfiguration.class,
        CredentialsProviderAutoConfiguration.class,
        RegionProviderAutoConfiguration.class
})
@ActiveProfiles("test")
public class S3FileStorageTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private S3Storage s3Storage;

    @Value("${test.s3.bucket-name}")
    private String bucketName;

    private final String testFileName = "dracula.epub";

    @Test
    public void testReadFile() throws IOException {
        String actualContent = s3Storage.readFIleFromS3(bucketName, testFileName);

        String data = actualContent.substring(0 , 300);
        System.out.println(data);

        assertNotNull(actualContent);
        assertEquals(300, data.length());
    }

    @Test
    void testResourceLoader() {
        Resource resource = resourceLoader.getResource(
                "s3://" + bucketName + "/dracula.epub"
        );

        System.out.println("Resource class: " + resource.getClass());
        System.out.println("Resource: " + resource);
    }

}
