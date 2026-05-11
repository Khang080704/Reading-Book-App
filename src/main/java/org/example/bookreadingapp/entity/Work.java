package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "works")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String workKey;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String coverId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "work_authors",
            joinColumns = @JoinColumn(name = "work_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Builder.Default
    private Set<AuthorDetail> authors = new HashSet<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Edition> editions = new ArrayList<>();

    public void addEdition(Edition edition) {
        editions.add(edition);
        edition.setWork(this);
    }

    public void removeEdition(Edition edition) {
        editions.remove(edition);
        edition.setWork(null);
    }
}

