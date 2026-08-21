package org.example.bookreadingapp.service.reader;

import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.service.reader.pdf.PdfChapterParser;
import org.example.bookreadingapp.service.reader.pdf.PdfDocumentReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
public class PdfDocumentReaderTest {


    @Test
    void parsePdf() {
        PdfChapterParser parser = new PdfChapterParser();
        PdfDocumentReader reader = new PdfDocumentReader(parser);
        Resource resource = new ClassPathResource("books/HP_And_Socceress_Stone.pdf");

        ParsedBook book = reader.read(resource);

        System.out.println("Title: " + book.title());

        System.out.println("Chapters: " + book.chapters().size());

        for (ParsedChapter chapter : book.chapters()) {
            System.out.println(
                    chapter.order()
                            + " - "
                            + chapter.title()
                            + " - "
                            + chapter.content().substring(0, Math.min(300, chapter.content().length()))
            );
        }
    }
}
