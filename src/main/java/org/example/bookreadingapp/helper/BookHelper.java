package org.example.bookreadingapp.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreadingapp.entity.Book;
import org.example.bookreadingapp.repository.BookRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookHelper {
    private final BookRepository bookRepository;

    @Async
    public void saveBook(List<Book> book) {
        bookRepository.saveAll(book);
    }
}
