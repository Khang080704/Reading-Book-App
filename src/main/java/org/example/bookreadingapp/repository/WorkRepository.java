package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {
    @EntityGraph(attributePaths = "authors")
    Optional<Work> findByWorkKey(String workKey);

    boolean existsByWorkKey(String workKey);

    @Override
    @EntityGraph(attributePaths = "authors")
    List<Work> findAll();

    Page<Work> findByAuthors_Id(String authorId, Pageable pageable);

    long countByAuthors_Id(String authorId);
}

