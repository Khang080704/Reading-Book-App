package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookreadingapp.Enum.ResourceProvider;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String olKey;

    private String birthDay;
    private String website;
    private String fullName;
    @Column(length = 2000)
    private String bio;

    @Column(name = "resource_provider")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ResourceProvider resourceProvider = ResourceProvider.OPEN_LIBRARY;

    private LocalDateTime createdAt;
    private LocalDateTime lastModify;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Work> works = new HashSet<>();
}
