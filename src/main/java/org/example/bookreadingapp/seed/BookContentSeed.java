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
    }

    private void seedDracula() {

        String workKey = "OL85892W";

        Work work = workRepository
                .findByWorkKey(workKey)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Seed work not found: " + workKey
                        )
                );

        boolean alreadyImported = readingResourceRepository
                .existsByWorkIdAndResourceProvider(work.getId(), ResourceProvider.GUTENBERG);

        if (alreadyImported) {
            log.info("Already imported book content");
            return;
        }

        Resource resource =
                new ClassPathResource(
                        "books/dracula.epub"
                );

        bookImportService.importEpub(
                workKey,
                resource
        );
    }
}
