package org.example.bookreadingapp.service.reader;

import org.example.bookreadingapp.Enum.BookFormat;
import org.example.bookreadingapp.dto.reading.ParsedBook;
import org.springframework.core.io.Resource;

public interface BookDocumentReader {
    boolean support(BookFormat bookFormat);
    ParsedBook read(Resource resource);
}
