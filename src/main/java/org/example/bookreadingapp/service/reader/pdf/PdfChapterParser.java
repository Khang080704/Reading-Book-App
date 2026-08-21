package org.example.bookreadingapp.service.reader.pdf;

import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.dto.reading.PdfPage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PdfChapterParser {
    public ParsedBook parse(Resource resource, List<PdfPage> pages) {
        List<ParsedChapter> chapters = new ArrayList<>();

        String currentTitle = null;
        StringBuilder currentContent = new StringBuilder();

        int chapterOrder = 0;

        for (PdfPage page : pages) {

            String text = page.content();

            if (text == null || text.isBlank()) {
                continue;
            }

            String[] lines = text.split("\\n");

            if (lines.length == 0) {
                continue;
            }

            String firstLine = normalizeLine(lines[0]);

            if (isChapterHeading(firstLine, lines)) {

                // đóng chapter trước
                if (currentTitle != null) {

                    chapters.add(
                            new ParsedChapter(
                                    ++chapterOrder,
                                    currentTitle,
                                    currentContent.toString().trim()
                            )
                    );
                }

                currentTitle = firstLine;
                currentContent = new StringBuilder();

                // bỏ heading khỏi content
                appendLines(currentContent, lines, 1);

            } else if (currentTitle != null) {

                appendLines(currentContent, lines, 0);
            }
        }

        // add chapter cuối
        if (currentTitle != null) {

            chapters.add(
                    new ParsedChapter(
                            ++chapterOrder,
                            currentTitle,
                            currentContent.toString().trim()
                    )
            );
        }

        return new ParsedBook(
                extractTitle(resource),
                null,
                null,
                chapters
        );
    }

    private boolean isChapterHeading(
            String firstLine,
            String[] lines
    ) {

        if (firstLine == null || firstLine.isBlank()) {
            return false;
        }

        // heading không nên quá dài
        if (firstLine.length() > 80) {
            return false;
        }

        // tránh những page gần như không có content
        if (lines.length < 3) {
            return false;
        }

        String lettersOnly =
                firstLine.replaceAll("[^\\p{L}]", "");

        if (lettersOnly.length() < 3) {
            return false;
        }

        /*
         * Heading trong file hiện tại chủ yếu uppercase.
         *
         * Ví dụ:
         * C ỨA BÉ VẪN SỐNG
         * C A ẤM KÍNH BIẾN MẤT
         */
        return isMostlyUpperCase(firstLine);
    }

    private boolean isMostlyUpperCase(String text) {

        int letters = 0;
        int upperCase = 0;

        for (char c : text.toCharArray()) {

            if (Character.isLetter(c)) {

                letters++;

                if (Character.isUpperCase(c)) {
                    upperCase++;
                }
            }
        }

        if (letters == 0) {
            return false;
        }

        double ratio =
                (double) upperCase / letters;

        return ratio >= 0.85;
    }

    private void appendLines(
            StringBuilder builder,
            String[] lines,
            int start
    ) {

        for (int i = start; i < lines.length; i++) {

            String line = normalizeLine(lines[i]);

            if (line.isBlank()) {
                continue;
            }

            if (!builder.isEmpty()) {
                builder.append("\n");
            }

            builder.append(line);
        }
    }

    private String normalizeLine(String line) {

        return line
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String extractTitle(Resource resource) {

        String filename = resource.getFilename();

        if (filename == null) {
            return "Unknown";
        }

        return filename.replaceFirst(
                "(?i)\\.pdf$",
                ""
        );
    }
}
