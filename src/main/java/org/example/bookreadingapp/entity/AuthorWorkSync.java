package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "author_work_sync")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthorWorkSync {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "has_next")
    private boolean hasNext;

    @Builder.Default
    @Column(nullable = false)
    private Integer nextOffset = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer batchSize = 50;

    @Column(name = "last_sync_at")
    private Instant lastSyncAt;

    @OneToOne
    @JoinColumn(name = "author_id", nullable = false, unique = true)
    private AuthorDetail authorDetail;

    @Column(name = "total_worl")
    private Long totalWork;

}
