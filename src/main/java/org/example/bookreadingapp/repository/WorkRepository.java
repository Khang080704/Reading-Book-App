package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, String> {
    @EntityGraph(attributePaths = "authors")
    Optional<Work> findByWorkKey(String workKey);
}

