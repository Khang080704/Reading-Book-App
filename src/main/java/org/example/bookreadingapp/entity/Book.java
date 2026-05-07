package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Book entity - simplified for search functionality
 * Stores search results and user's book library
 * Not linked to Work anymore - Works are accessed through Author
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Open Library book key (from search results)
     * e.g., "/works/OL82563W"
     */
    @Column(unique = true)
    private String bookKey;

    private String title;

    private String isbn;

    private Integer editionCount;

    private Integer firstPublishYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
}
