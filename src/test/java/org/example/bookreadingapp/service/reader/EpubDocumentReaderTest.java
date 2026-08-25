package org.example.bookreadingapp.service.reader;

import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.service.reader.epub.EpubDocumentReader;
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


        assert !book.chapters().isEmpty();

        for (ParsedChapter chapter :
                book.chapters()) {

            assert chapter.content().length() == 300;
        }
    }

    @Test
    void testReadEpubWithHarryPotter() {

        String[] data = {
                "books/HP_And_Champer_Secret.epub",
                "books/HP_And_Goblet_Of_Fire.epub",
                "books/HP_And_Prison_Of_Azkaban.epub",
                "books/HP_And_Halfblood_Prince.epub"
        };

        for(String item : data) {
            Resource resource =
                    new ClassPathResource(
                            item
                    );
            EpubDocumentReader reader =
                    new EpubDocumentReader();

            ParsedBook book = reader.read(resource);


            assert !book.chapters().isEmpty();

            for (ParsedChapter chapter :
                    book.chapters()) {

                assert !chapter.title().isEmpty();
                assert !chapter.content().isEmpty();
            }

        }

    }
}
