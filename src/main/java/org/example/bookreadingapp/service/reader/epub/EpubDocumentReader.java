package org.example.bookreadingapp.service.reader.epub;

import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.Enum.BookFormat;
import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.example.bookreadingapp.dto.reading.ParsedChapter;
import org.example.bookreadingapp.dto.reading.TocEntry;
import org.example.bookreadingapp.service.reader.BookDocumentReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
@Component
public class EpubDocumentReader implements BookDocumentReader {

    @Override
    public boolean support(BookFormat bookFormat) {
        return bookFormat == BookFormat.EPUB;
    }

    public ParsedBook read(Resource resource) {
        Path tempFile = null;

        try {

            tempFile =
                    Files.createTempFile(
                            "book-",
                            ".epub"
                    );

            try (InputStream input =
                         resource.getInputStream()) {

                Files.copy(
                        input,
                        tempFile,
                        StandardCopyOption
                                .REPLACE_EXISTING
                );
            }

            try (ZipFile zip =
                         new ZipFile(
                                 tempFile.toFile()
                         )) {

                String opfPath = findOpfPath(zip);

                org.w3c.dom.Document opf =
                        parseXml(
                                zip,
                                opfPath
                        );

                String title =
                        firstText(
                                opf,
                                "title"
                        );

                String author =
                        firstText(
                                opf,
                                "creator"
                        );

                String language =
                        firstText(
                                opf,
                                "language"
                        );

                Map<String, ManifestItem> manifest = parseManifest(opf);

                List<TocEntry> toc =
                        parseNavigation(
                                zip,
                                opfPath,
                                manifest
                        );

                List<ParsedChapter> chapters =
                        parseChaptersFromToc(
                                zip,
                                opfPath,
                                toc
                        );

                return new ParsedBook(
                        title,
                        author,
                        language,
                        chapters
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse EPUB",
                    e
            );

        } finally {

            if (tempFile != null) {
                try {
                    Files.deleteIfExists(
                            tempFile
                    );
                } catch (Exception ignored) {
                }
            }
        }
    }

    // =========================================================
    // 1. container.xml -> OPF path
    // =========================================================

    private String findOpfPath(ZipFile zip) throws Exception {

        ZipEntry entry = zip.getEntry("META-INF/container.xml");

        if (entry == null) {
            throw new IllegalArgumentException(
                    "Invalid EPUB: META-INF/container.xml not found"
            );
        }

        org.w3c.dom.Document document =
                parseXml(zip, entry.getName());

        NodeList rootFiles =
                document.getElementsByTagNameNS(
                        "*",
                        "rootfile"
                );

        if (rootFiles.getLength() == 0) {
            throw new IllegalArgumentException(
                    "Invalid EPUB: OPF rootfile not found"
            );
        }

        org.w3c.dom.Element rootFile =
                (org.w3c.dom.Element) rootFiles.item(0);

        return rootFile.getAttribute("full-path");
    }

    // =========================================================
    // 2. Manifest
    // =========================================================

    private Map<String, ManifestItem> parseManifest(org.w3c.dom.Document opf) {

        Map<String, ManifestItem> result =
                new HashMap<>();

        NodeList items =
                opf.getElementsByTagNameNS(
                        "*",
                        "item"
                );

        for (int i = 0; i < items.getLength(); i++) {

            org.w3c.dom.Element item = (org.w3c.dom.Element) items.item(i);

            String id = item.getAttribute("id");

            String href = item.getAttribute("href");

            String mediaType = item.getAttribute("media-type");

            String properties = item.getAttribute("properties");

            result.put(
                    id,
                    new ManifestItem(
                            id,
                            href,
                            mediaType,
                            properties
                    )
            );
        }

        return result;
    }

    // =========================================================
    // XML
    // =========================================================

    private org.w3c.dom.Document parseXml(ZipFile zip, String path) throws Exception {

        ZipEntry entry = zip.getEntry(path);

        if (entry == null) {
            throw new IllegalArgumentException(
                    "EPUB entry not found: " + path
            );
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setNamespaceAware(true);

        // chống XXE
        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
        );

        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false
        );

        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false
        );

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_DTD,
                ""
        );

        factory.setAttribute(
                XMLConstants.ACCESS_EXTERNAL_SCHEMA,
                ""
        );

        try (InputStream input =
                     zip.getInputStream(entry)) {

            return factory
                    .newDocumentBuilder()
                    .parse(input);
        }
    }

    // =========================================================
    // Metadata helper
    // =========================================================

    private String firstText(org.w3c.dom.Document document, String localName) {

        NodeList nodes =
                document.getElementsByTagNameNS(
                        "*",
                        localName
                );

        if (nodes.getLength() == 0) {
            return null;
        }

        String value = nodes
                        .item(0)
                        .getTextContent();

        return value == null
                ? null
                : value.trim();
    }

    // =========================================================
    // Path helper
    // =========================================================

    private String getParentPath(String path) {

        int index =
                path.lastIndexOf('/');

        if (index == -1) {
            return "";
        }

        return path.substring(
                0,
                index
        );
    }

    private String resolvePath(String parent, String href) {

        if (parent.isBlank()) {
            return href;
        }

        return Path
                .of(parent)
                .resolve(href)
                .normalize()
                .toString()
                .replace("\\", "/");
    }

    private ManifestItem findNavigationItem(Map<String, ManifestItem> manifest) {

        return manifest
                .values()
                .stream()
                .filter(item -> {
                        if (item.properties().equals("nav")) {
                            return true;
                        }
                        if(item.href().equals("toc.xhtml")) {
                            return true;
                        }
                        if(item.href().equals("toc.ncx")) {
                            return true;
                        }
                        return false;
                    }
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "EPUB navigation document not found"
                        )
                );
    }

    private List<TocEntry> parseNavigation(ZipFile zip, String opfPath, Map<String, ManifestItem> manifest) throws Exception {

        ManifestItem navItem = findNavigationItem(manifest);

        String opfDirectory = getParentPath(opfPath);

        String navPath = resolvePath(
                opfDirectory,
                navItem.href()
        );
        log.info("navPath: {}", navPath);

        ZipEntry entry = zip.getEntry(navPath);

        if (entry == null) {
            throw new IllegalArgumentException(
                    "Navigation file not found: "
                            + navPath
            );
        }

        try (InputStream input = zip.getInputStream(entry)) {

            org.jsoup.nodes.Document document =
                    Jsoup.parse(
                            input,
                            "UTF-8",
                            ""
                    );

            /*
             * EPUB3:
             *
             * <nav epub:type="toc">
             */
            Element toc = document.selectFirst("nav[epub\\:type=toc]");

            // Một số EPUB dùng role="doc-toc"
            if (toc == null) {
                toc = document.selectFirst("nav[role=doc-toc]");
            }

            if(toc == null) {
                toc = document.selectFirst("navmap");
            }

            if (toc == null) {
                throw new IllegalArgumentException(
                        "TOC navigation not found"
                );
            }

            List<TocEntry> result = new ArrayList<>();

            for (Element link : toc.select("a[href]")) {
                String title = link.text().trim();

                String href = link.attr("href").trim();

                if (title.isBlank() || href.isBlank()) {
                    continue;
                }

                result.add(new TocEntry(title, href));
            }

            if(result.isEmpty()) {
                for(Element link : toc.select("navPoint")) {
                    String title = link.selectFirst("text").text().trim();
                    String content = link.selectFirst("content").attr("src").trim();
                    result.add(new TocEntry(title, content));
                }
            }

            return result;
        }
    }

    private List<ParsedChapter> parseChaptersFromToc(
            ZipFile zip,
            String opfPath,
            List<TocEntry> toc
    ) throws Exception {

        List<ParsedChapter> result = new ArrayList<>();

        String opfDirectory = getParentPath(opfPath);

        int order = 0;

        for (TocEntry tocEntry : toc) {

            String href = tocEntry.href();

            String fileHref =
                    removeFragment(href);

            String xhtmlPath =
                    resolvePath(
                            opfDirectory,
                            fileHref
                    );

            ZipEntry entry =
                    zip.getEntry(xhtmlPath);

            if (entry == null) {
                continue;
            }

            org.jsoup.nodes.Document document;

            try (InputStream input =
                         zip.getInputStream(entry)) {

                document =
                        Jsoup.parse(
                                input,
                                "UTF-8",
                                ""
                        );
            }

            Element chapterElement =
                    findChapterElement(
                            document,
                            href
                    );


            String content =
                    extractParagraphs(
                            document
                    );

            if (content.isBlank()) {
                continue;
            }

//            String title =
//                    extractChapterTitle(
//                            chapterElement,
//                            tocEntry.title()
//                    );

            result.add(
                    new ParsedChapter(
                            order++,
                            tocEntry.title(),
                            content
                    )
            );
        }

        return result;
    }

    private String extractChapterTitle(Element chapter, String fallbackTitle) {

        Element heading =
                chapter.selectFirst(
                        "h1, h2, h3, h4, h5, h6"
                );

        if (heading != null) {

            String title =
                    heading.text().trim();

            if (!title.isBlank()) {
                return title;
            }
        }

        return fallbackTitle;
    }

    private Element findChapterElement(org.jsoup.nodes.Document document, String href) {

        String fragment = extractFragment(href);

        Element target;

        /*
         * Nếu TOC trỏ tới:
         *
         * chapter.xhtml#chapter-1
         */
        if (fragment != null) {

            target = document.getElementById(fragment);

            if (target == null) {
                return null;
            }

        } else {

            target = document.body();
        }

        /*
         * Chính target là chapter:
         *
         * <section
         *      id="chapter-1"
         *      epub:type="chapter">
         */
        if (isChapterElement(target)) {
            return target;
        }

        /*
         * target có thể là heading nằm bên trong:
         *
         * <section epub:type="chapter">
         *     <h2 id="chapter-1">
         */
        Element parent = target.parent();

        while (parent != null) {

            if (isChapterElement(parent)) {
                return parent;
            }

            parent = parent.parent();
        }

        /*
         * Hoặc target bao ngoài section chapter.
         */
        Element childChapter =
                target.selectFirst(
                        "[epub\\:type~=chapter], " +
                                "[role=doc-chapter]"
                );

        return childChapter;
    }

    private boolean isChapterElement(Element element) {

        if (element == null) {
            return false;
        }

        String epubType = element.attr("epub:type");

        boolean semanticChapter =
                !epubType.isBlank()
                        && Arrays.stream(
                                epubType.split("\\s+")
                        )
                        .anyMatch(
                                "chapter"::equals
                        );

        boolean roleChapter =
                "doc-chapter".equals(
                        element.attr("role")
                );

        boolean classChapter =
                element.hasClass("chapter");

        return semanticChapter
                || roleChapter
                || classChapter;
    }

    private String removeFragment(String href) {

        int index =
                href.indexOf('#');

        if (index == -1) {
            return href;
        }

        return href.substring(
                0,
                index
        );
    }

    private String extractFragment(String href) {

        int index =
                href.indexOf('#');

        if (index == -1
                || index == href.length() - 1) {
            return null;
        }

        return href.substring(
                index + 1
        );
    }

    private String extractParagraphs(Element element) {

        return element
                .select("p")
                .stream()
                .map(Element::text)
                .map(String::trim)
                .filter(text ->
                        !text.isBlank()
                )
                .collect(
                        Collectors.joining(
                                "\n\n"
                        )
                );
    }

    private record ManifestItem(
            String id,
            String href,
            String mediaType,
            String properties
    ) {
    }
}