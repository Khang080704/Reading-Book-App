package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.AuthorWorkSync;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorWorkSyncRepository extends JpaRepository<AuthorWorkSync, String> {

    Optional<AuthorWorkSync> findByAuthorDetail_Id(
            String authorId
    );

    Optional<AuthorWorkSync> findByAuthorDetail_OlKey(
            String olKey
    );
}
