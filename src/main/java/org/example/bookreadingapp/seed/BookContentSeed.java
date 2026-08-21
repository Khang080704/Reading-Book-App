package org.example.bookreadingapp.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.Enum.ResourceProvider;
import org.example.bookreadingapp.entity.Work;
import org.example.bookreadingapp.repository.ReadingResourceRepository;
import org.example.bookreadingapp.repository.WorkRepository;
import org.example.bookreadingapp.service.BookImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookContentSeed implements CommandLineRunner {
    private final WorkRepository workRepository;
    private final ReadingResourceRepository readingResourceRepository;
    private final BookImportService bookImportService;

    @Override
    public void run(String... args) throws Exception {

        seedDracula();
        seedHarryPotterAndSorcererStone();
        seedHarryPotterAndChampterOfSecret();
    }

    private void seedDracula() {

        String workKey = "OL85892W";
        String classPath = "books/dracula.epub";
        seedData(workKey, classPath, ResourceProvider.GUTENBERG);
    }

    private void seedHarryPotterAndSorcererStone() {
        String workKey = "OL82563W";
        String classPath = "books/HP_And_Soceress_Stone.epub";
        seedData(workKey, classPath, ResourceProvider.INTERNAL);
    }
    private void seedHarryPotterAndChampterOfSecret() {
        String workKey = "OL82537W";
        String classPath = "books/HP_And_Champer_Secret.epub";
        seedData(workKey, classPath, ResourceProvider.INTERNAL);
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
                .existsByWorkIdAndResourceProvider(work.getId(), ResourceProvider.GUTENBERG);

        log.info("Book import status for title {} is {}", work.getTitle(), alreadyImported);

        if (alreadyImported) {
            log.info("Already imported book content");
            return;
        }

        Resource resource = new ClassPathResource(classPath);

        bookImportService.importEpub(
                workKey,
                resource,
                resourceProvider
        );
    }
}
