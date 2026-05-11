package org.example.bookreadingapp.repository;

import org.example.bookreadingapp.entity.AuthorDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorDetailRepository extends JpaRepository<AuthorDetail,String> {
    Optional<AuthorDetail> findByOlKey(String olKey);
}
