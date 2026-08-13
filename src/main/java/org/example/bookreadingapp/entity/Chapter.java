package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private ReadingResource readingResource;
}
