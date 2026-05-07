package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    /**
     * Find book by book key from Open Library
     */
    Optional<Book> findByBookKey(String bookKey);

    @EntityGraph(attributePaths = "author")
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
