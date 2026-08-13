package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.ReadingResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadingResourceRepository extends JpaRepository<ReadingResource, String> {

    @Query("SELECT  r from ReadingResource r join fetch r.work w where w.workKey = :workKey and r.isPreferred = true")
    Optional<ReadingResource> findByWorkKey(String workKey);
}
