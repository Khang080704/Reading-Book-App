package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.Enum.ResourceProvider;
import org.example.bookreadingapp.entity.ReadingResource;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadingResourceRepository extends JpaRepository<ReadingResource, String> {

    @Query("SELECT  r from ReadingResource r join fetch r.work w where w.workKey = :workKey")
    Optional<ReadingResource> findByWorkKey(String workKey);

    @EntityGraph(attributePaths = {"chapters"})
    @Query("select r from ReadingResource r where r.id = :readingResourceId")
    ReadingResource getChaptersByReadingResourceId(String readingResourceId);

    boolean existsByWorkIdAndResourceProvider(String workId, ResourceProvider provider);
}
