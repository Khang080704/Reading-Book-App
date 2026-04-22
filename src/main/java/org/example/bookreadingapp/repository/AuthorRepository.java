package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,String> {
    List<Author> findByNameContainingIgnoreCase(String authorName);

    Optional<Author> findByOlKey(String olKey);
}
