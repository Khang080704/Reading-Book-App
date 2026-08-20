package org.example.bookreadingapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.bookreadingapp.Enum.ReadingMode;
import org.example.bookreadingapp.Enum.ResourceProvider;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reading_resource")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReadingResource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_key")
    private Work work;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private ResourceProvider resourceProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "reading_mode")
    private ReadingMode readingMode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "readingResource", orphanRemoval = true)
    @Builder.Default
    private List<Chapter> chapters = new ArrayList<>();

    public void addChapter(Chapter chapter) {
        chapters.add(chapter);
        chapter.setReadingResource(this);
    }

    public void clearChapters() {
        chapters.clear();
    }
}
