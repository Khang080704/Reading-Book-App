package org.example.bookreadingapp.service;

import org.example.bookreadingapp.client.BookApiClient;
import org.example.bookreadingapp.dto.book.SearchBookDTO;
import org.example.bookreadingapp.dto.book.SearchBooksDTO;
import org.example.bookreadingapp.entity.Author;
import org.example.bookreadingapp.entity.Book;
import org.example.bookreadingapp.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private BookApiClient bookApiClient;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    void searchBooks_should_return_local_database_results_when_available() {
        Author author = new Author();
        author.setName("J. K. Rowling");

        Book book = Book.builder()
                .bookKey("/works/OL82563W")
                .title("Harry Potter and the Philosopher's Stone")
                .isbn("9780747532699")
                .editionCount(275)
                .firstPublishYear(1997)
                .author(author)
                .build();

        Page<Book> localPage = new PageImpl<>(List.of(book), PageRequest.of(0, 10), 1);
        when(bookRepository.findByTitleContainingIgnoreCase(eq("harry potter"), eq(PageRequest.of(0, 10))))
                .thenReturn(localPage);

        List<SearchBookDTO> results = searchService.searchBooks("harry potter", 1, 10);

        assertEquals(1, results.size());
        SearchBookDTO dto = results.get(0);
        assertEquals("/works/OL82563W", dto.getBookKey());
        assertEquals("Harry Potter and the Philosopher's Stone", dto.getTitle());
        assertArrayEquals(new String[]{"J. K. Rowling"}, dto.getAuthorNames());
        assertEquals(Integer.valueOf(1997), dto.getFirstPublishYear());
        assertEquals("9780747532699", dto.getIsbn());
        assertEquals(Integer.valueOf(275), dto.getEditionCount());
        assertNull(dto.getCoverUrl());

        verify(bookApiClient, never()).searchBooks(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt());
        verify(bookRepository, never()).saveAll(anyList());
    }

    @Test
    void searchBooks_should_fetch_from_api_and_save_results_when_local_cache_misses() {
        when(bookRepository.findByTitleContainingIgnoreCase(eq("tolkien"), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        SearchBooksDTO.BookSearchEntry entry = new SearchBooksDTO.BookSearchEntry();
        entry.setKey("/works/OL27448W");
        entry.setTitle("The Hobbit");
        entry.setAuthorNames(List.of("J. R. R. Tolkien"));
        entry.setFirstPublishYear(1937);
        entry.setIsbn(List.of("9780547928227"));
        entry.setEditionCount(300);
        entry.setCoverId(123L);

        SearchBooksDTO response = new SearchBooksDTO();
        response.setNumFound(1);
        response.setDocs(List.of(entry));
        when(bookApiClient.searchBooks("tolkien", 1, 10)).thenReturn(response);
        when(bookRepository.findByBookKey("/works/OL27448W")).thenReturn(Optional.empty());
        when(bookRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        List<SearchBookDTO> results = searchService.searchBooks("tolkien", 1, 10);

        assertEquals(1, results.size());
        SearchBookDTO dto = results.get(0);
        assertEquals("/works/OL27448W", dto.getBookKey());
        assertEquals("The Hobbit", dto.getTitle());
        assertArrayEquals(new String[]{"J. R. R. Tolkien"}, dto.getAuthorNames());
        assertEquals(Integer.valueOf(1937), dto.getFirstPublishYear());
        assertEquals("9780547928227", dto.getIsbn());
        assertEquals(Integer.valueOf(300), dto.getEditionCount());
        assertEquals("https://covers.openlibrary.org/b/id/123-M.jpg", dto.getCoverUrl());

        ArgumentCaptor<List<Book>> bookCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepository).saveAll(bookCaptor.capture());
        List<Book> savedBooks = bookCaptor.getValue();
        assertEquals(1, savedBooks.size());
        assertEquals("/works/OL27448W", savedBooks.get(0).getBookKey());
        assertEquals("The Hobbit", savedBooks.get(0).getTitle());
        assertEquals("9780547928227", savedBooks.get(0).getIsbn());
        assertEquals(Integer.valueOf(300), savedBooks.get(0).getEditionCount());
        assertEquals(Integer.valueOf(1937), savedBooks.get(0).getFirstPublishYear());
    }
}


