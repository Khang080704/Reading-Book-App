package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, String> {
    @Query("select c from Chapter c where c.indexOrder < :order and c.readingResource.id = :resourceId")
    Optional<Chapter> findPreviousChapter(int order, String resourceId);

    @Query("select c from Chapter c where c.indexOrder > :order and c.readingResource.id = :resourceId")
    Optional<Chapter> findNextChapter(int order, String resourceId);
}
