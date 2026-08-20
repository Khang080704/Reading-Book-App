package org.example.bookreadingapp.service.reader;

import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
public class EpubDocumentReaderTest {
    @Test
    void testReadEpubWithDracula() {

        Resource resource =
                new ClassPathResource(
                        "books/dracula.epub"
                );

        EpubDocumentReader reader =
                new EpubDocumentReader();

        ParsedBook book =
                reader.read(resource);

        System.out.println(
                "Title: " + book.title()
        );

        System.out.println(
                "Author: " + book.author()
        );

        System.out.println(
                "Language: " + book.language()
        );

        System.out.println(
                "Chapters: " + book.chapters().size()
        );

        for (ParsedChapter chapter :
                book.chapters()) {

            System.out.println(
                    "\n===== "
                            + chapter.order()
                            + " - "
                            + chapter.title()
                            + " ====="
            );

            System.out.println(
                    chapter.content()
                            .substring(
                                    0,
                                    Math.min(
                                            300,
                                            chapter.content().length()
                                    )
                            )
            );
        }
    }

    @Test
    void testReadEpubWith() {

    }
}
