package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "editions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Edition {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String editionKey;

    private String isbn;

    private Integer numberOfPages;

    private String publishDate;

    private String publisherName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Work work;
}

