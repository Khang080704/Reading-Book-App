package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditionRepository extends JpaRepository<Edition, String> {
    Optional<Edition> findByEditionKey(String editionKey);

    List<Edition> findByWork_WorkKey(String workKey);
}

