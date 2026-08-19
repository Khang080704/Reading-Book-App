package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Work;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {
    @EntityGraph(attributePaths = "authors")
    Optional<Work> findByWorkKey(String workKey);

    boolean existsByWorkKey(String workKey);


    @Query("select w from Work w join fetch w.readingResources r")
    List<Work> getAvailableBooks();

    @BatchSize(size = 20)
    Page<Work> findByAuthors_OlKey(String olKey, Pageable pageable);

    long countByAuthors_OlKey(String authorId);
}

