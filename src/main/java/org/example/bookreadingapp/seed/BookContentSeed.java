package org.example.bookreadingapp.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.Enum.BookFormat;
import org.example.bookreadingapp.Enum.ResourceProvider;
import org.example.bookreadingapp.Enum.StorageType;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.service.BookImportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class BookContentSeed implements CommandLineRunner {
    private final WorkRepository workRepository;
    private final ReadingResourceRepository readingResourceRepository;
    private final BookImportService bookImportService;
    private final String storageClasspath;

    private final String bucket = "s3-bookverse-295470186147-ap-southeast-2-an";

    public BookContentSeed(WorkRepository workRepository, ReadingResourceRepository readingResourceRepository,
                           BookImportService bookImportService, @Value("book.storage.filesystem.root") String storageClasspath) {
        this.bookImportService = bookImportService;
        this.workRepository = workRepository;
        this.readingResourceRepository = readingResourceRepository;
        this.storageClasspath = storageClasspath;
    }

    @Override
    public void run(String... args) throws Exception {

        seedDracula();
        seedHarryPotterAndSorcererStone();
        seedHarryPotterAndChampterOfSecret();
        seedHarryPotterAndGobletOfFire();
        seedHarryPotterAndHalfBloodPrince();
    }

    private void seedDracula() {

        String workKey = "OL85892W";
        String fileName = "dracula.epub";

        seedDataWithAws(workKey, bucket, fileName, ResourceProvider.INTERNAL);
    }

    private void seedHarryPotterAndSorcererStone() {
        String workKey = "OL82563W";
        String fileName = "HP_And_Soceress_Stone.epub";

        seedDataWithAws(workKey, bucket, fileName, ResourceProvider.INTERNAL);

    }
    private void seedHarryPotterAndChampterOfSecret() {
        String workKey = "OL82537W";
        String fileName = "HP_And_Champer_Secret.epub";

        seedDataWithAws(workKey, bucket, fileName, ResourceProvider.INTERNAL);
    }
    private void seedHarryPotterAndGobletOfFire() {
        String workKey = "OL82560W";
        String fileName = "HP_And_Goblet_Of_Fire.epub";

        seedDataWithAws(workKey, bucket, fileName, ResourceProvider.INTERNAL);

    }
    private void seedHarryPotterAndHalfBloodPrince(){
        String workKey = "OL82565W";
        String fileName = "HP_And_Halfblood_Prince.epub";

        seedDataWithAws(workKey, bucket, fileName, ResourceProvider.INTERNAL);
    }

    private void seedData(String workKey, String classPath, ResourceProvider resourceProvider) {
        Work work = workRepository
                .findByWorkKey(workKey)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Seed work not found: " + workKey
                        )
                );

        boolean alreadyImported = readingResourceRepository
                .existsByWorkIdAndResourceProvider(work.getId(), resourceProvider);

        log.info("Book import status for title {} is {}", work.getTitle(), alreadyImported);

        if (alreadyImported) {
            log.info("Already imported book content");
            return;
        }

        bookImportService.importEpub(
                workKey,
                BookFormat.EPUB,
                StorageType.CLASSPATH,
                classPath,
                ResourceProvider.INTERNAL
        );
    }

    private void seedDataWithAws(String workKey, String bucket, String fileName, ResourceProvider resourceProvider) {
        Work work = workRepository
                .findByWorkKey(workKey)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Seed work not found: " + workKey
                        )
                );

        boolean alreadyImported = readingResourceRepository
                .existsByWorkIdAndResourceProvider(work.getId(), resourceProvider);

        log.info("Book import status for title {} is {}", work.getTitle(), alreadyImported);

        if (alreadyImported) {
            log.info("Already imported book content");
            return;
        }

        String s3ClassPath = String.format("s3://%s/%s", bucket, fileName);
        bookImportService.importEpub(
                workKey,
                BookFormat.EPUB,
                StorageType.S3,
                s3ClassPath,
                ResourceProvider.INTERNAL
        );
    }
}
