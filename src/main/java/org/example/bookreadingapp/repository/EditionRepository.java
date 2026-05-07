package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditionRepository extends JpaRepository<Edition, String> {
    List<Edition> findByWork_WorkKey(String workKey);
}

