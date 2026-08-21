package org.example.bookreadingapp.service.reader.pdf;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.bookreadingapp.Enum.BookFormat;
import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.PdfPage;
import org.example.bookreadingapp.service.reader.BookDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PdfDocumentReader implements BookDocumentReader {
    private final PdfChapterParser pdfChapterParser;

    @Override
    public boolean support(BookFormat bookFormat) {
        return bookFormat == BookFormat.PDF;
    }

    @Override
    public ParsedBook read(Resource resource) {
        try {
            List<PdfPage> pages = extractPages(resource);

            return pdfChapterParser.parse(resource, pages);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to read PDF: " + resource.getFilename(),
                    e
            );
        }
    }

    private List<PdfPage> extractPages(Resource resource) throws IOException {

        byte[] data = resource.getInputStream().readAllBytes();

        try (PDDocument document = Loader.loadPDF(data)) {

            PDFTextStripper stripper = new PDFTextStripper();

            List<PdfPage> pages = new ArrayList<>();

            for (int pageNumber = 1;
                 pageNumber <= document.getNumberOfPages();
                 pageNumber++) {

                stripper.setStartPage(pageNumber);
                stripper.setEndPage(pageNumber);

                String content = stripper.getText(document);

                pages.add(
                        new PdfPage(
                                pageNumber,
                                normalize(content)
                        )
                );
            }

            return pages;
        }
    }

    private String normalize(String text) {

        if (text == null) {
            return "";
        }

        return text
                .replace("\r\n", "\n")
                .replace('\r', '\n')
                .replace("\u00A0", " ")
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
    }
}
